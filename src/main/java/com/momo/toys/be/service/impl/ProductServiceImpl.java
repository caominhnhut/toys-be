package com.momo.toys.be.service.impl;

import com.momo.toys.be.entity.CategoryEntity;
import com.momo.toys.be.entity.DocumentEntity;
import com.momo.toys.be.entity.ProductEntity;
import com.momo.toys.be.enumeration.EntityStatus;
import com.momo.toys.be.exception.FileStorageException;
import com.momo.toys.be.factory.CommonUtility;
import com.momo.toys.be.factory.mapper.DocumentMapper;
import com.momo.toys.be.factory.mapper.ProductMapper;
import com.momo.toys.be.model.Document;
import com.momo.toys.be.model.Product;
import com.momo.toys.be.repository.CategoryRepository;
import com.momo.toys.be.repository.ProductRepository;
import com.momo.toys.be.service.AccountService;
import com.momo.toys.be.service.DocumentService;
import com.momo.toys.be.service.ProductService;
import javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Autowired
    private CommonUtility commonUtility;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private DocumentService documentService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private AccountService accountService;

    @Override
    public Long create(Product product) throws NotFoundException, InterruptedException {
        Optional<CategoryEntity> categoryEntityOptional = categoryRepository.findById(product.getCategoryId());
        if (!categoryEntityOptional.isPresent()) {
            throw new NotFoundException(String.format("Category with id [%s] not found", product.getCategoryId()));
        }

        for (Document image : product.getImages()) {
            String extension = StringUtils.getFilenameExtension(image.getFilename());
            String uniqueName = commonUtility.uniqueFileName.apply(extension);
            if (image.getFilename().equals(product.getMainImage())) {
                product.setMainImage(uniqueName);
            }
            image.setFilename(uniqueName);

            // sleep in 1 milliseconds to get risk of duplicating image name
            TimeUnit.MILLISECONDS.sleep(1);
        }

        ProductEntity productEntity = ProductMapper.mapModelToEntity.apply(product);
        productEntity.setCategoryEntity(categoryEntityOptional.get());
        productEntity.setCreatedBy(accountService.getAuthorizedAccount().getName());
        productRepository.save(productEntity);

        for (Document image : product.getImages()) {
            documentService.upload(image, productEntity);
        }

        return productEntity.getId();
    }

    @Override
    public Long update(Product product) throws NotFoundException, FileStorageException {

        Optional<ProductEntity> existingProductEntityOptional = productRepository.findById(product.getId());

        if (!existingProductEntityOptional.isPresent()) {
            throw new NotFoundException(String.format("Product with id [%s] not found", product.getId()));
        }

        ProductEntity productEntity = existingProductEntityOptional.get();
        convertFromModelToEntity.accept(product, productEntity);

        List<Document> images = product.getImages();
        if (images == null || images.isEmpty()) {
            productRepository.save(productEntity);
            return productEntity.getId();
        }

        List<String> newUniqueNames = new ArrayList<>();
        for (Document image : images) {
            String extension = StringUtils.getFilenameExtension(image.getFilename());
            String uniqueName = commonUtility.uniqueFileName.apply(extension);
            if (image.getFilename().equals(product.getMainImage())) {
                productEntity.setMainImage(uniqueName);
            }
            image.setFilename(uniqueName);
            newUniqueNames.add(uniqueName);
            documentService.upload(image, productEntity);
        }

        productRepository.save(productEntity);

        // Delete existing images
        Set<DocumentEntity> existingImages = productEntity.getImages();
        for (DocumentEntity imageEntity : existingImages) {

            if (newUniqueNames.contains(imageEntity.getFilename())) {
                continue;
            }

            Document image = DocumentMapper.mapEntityToDocument.apply(imageEntity);
            documentService.delete(image);
        }

        return productEntity.getId();
    }

    @Override
    public Set<Product> findByCategory(Long categoryId, int offset, int limit) {

        Sort sortable = Sort.by("createdDate").descending();

        Pageable pageable = PageRequest.of(offset, limit, sortable);

        Page<ProductEntity> productEntities = productRepository.findByCategory(categoryId, pageable);

        return productEntities.stream().map(productEntity -> {
            Product productModel = ProductMapper.mapEntityToModel.apply(productEntity);
            List<Document> documents = productEntity.getImages().stream().map(DocumentMapper.mapEntityToDocument).collect(Collectors.toList());
            productModel.setImages(documents);
            return productModel;
        }).collect(Collectors.toSet());
    }

    @Override
    public Boolean softDelete(Long productId) throws NotFoundException {
        Optional<ProductEntity> existingProductEntityOptional = productRepository.findById(productId);

        if (!existingProductEntityOptional.isPresent()) {
            throw new NotFoundException(String.format("Product with id [%s] not found", productId));
        }

        ProductEntity productEntity = existingProductEntityOptional.get();
        productEntity.setStatus(EntityStatus.DELETED);
        productRepository.save(productEntity);
        return true;
    }

    private BiConsumer<Product, ProductEntity> convertFromModelToEntity = (product, productEntity) -> {
        productEntity.setName(product.getName());
        productEntity.setCode(product.getCode());
        productEntity.setAmount(product.getAmount());
        productEntity.setCostPrice(product.getCostPrice());
        productEntity.setPrice(product.getPrice());
        productEntity.setDescription(product.getDescription());
    };
}
