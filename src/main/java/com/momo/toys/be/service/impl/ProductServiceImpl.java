package com.momo.toys.be.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.momo.toys.be.entity.CategoryEntity;
import com.momo.toys.be.entity.DocumentEntity;
import com.momo.toys.be.entity.ProductEntity;
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

@Service
public class ProductServiceImpl implements ProductService{

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
    public Long create(Product product) throws NotFoundException{
        Optional<CategoryEntity> categoryEntityOptional = categoryRepository.findById(product.getCategoryId());
        if(!categoryEntityOptional.isPresent()){
            throw new NotFoundException(String.format("Category with id [%s] not found", product.getCategoryId()));
        }

        for(Document image : product.getImages()){
            String extension = StringUtils.getFilenameExtension(image.getFilename());
            String uniqueName = commonUtility.uniqueFileName.apply(extension);
            if(image.getFilename().equals(product.getMainImage())){
                product.setMainImage(uniqueName);
            }
            image.setFilename(uniqueName);
        }

        ProductEntity productEntity = ProductMapper.mapModelToEntity.apply(product);
        productEntity.setCategoryEntity(categoryEntityOptional.get());
        productEntity.setCreatedBy(accountService.getAuthorizedAccount().getName());
        productRepository.save(productEntity);

        for(Document image : product.getImages()){
            documentService.upload(image, productEntity);
        }

        return productEntity.getId();
    }

    @Override
    public Long update(Product product) throws NotFoundException, FileStorageException{
        Optional<ProductEntity> existingProductEntityOptional = productRepository.findById(product.getId());
        if(!existingProductEntityOptional.isPresent()){
            throw new NotFoundException(String.format("Product with id [%s] not found", product.getId()));
        }
        ProductEntity productEntity = existingProductEntityOptional.get();
        productEntity.setName(product.getName());
        productEntity.setCode(product.getCode());
        productEntity.setAmount(product.getAmount());
        productEntity.setCostPrice(product.getCostPrice());
        productEntity.setPrice(product.getPrice());
        productEntity.setDescription(product.getDescription());

        List<Document> images = product.getImages();
        if(images.isEmpty()){
            return productEntity.getId();
        }

        List<String> newUniqueNames = new ArrayList<>();
        for(Document image : images){
            String extension = StringUtils.getFilenameExtension(image.getFilename());
            String uniqueName = commonUtility.uniqueFileName.apply(extension);
            if(image.getFilename().equals(product.getMainImage())){
                productEntity.setMainImage(uniqueName);
            }
            image.setFilename(uniqueName);
            newUniqueNames.add(uniqueName);
            documentService.upload(image, productEntity);
        }

        productRepository.save(productEntity);

        // Delete existing images
        Set<DocumentEntity> existingImages = productEntity.getImages();
        for(DocumentEntity imageEntity : existingImages){

            if(newUniqueNames.contains(imageEntity.getFilename())){
                continue;
            }

            Document image = DocumentMapper.mapEntityToDocument.apply(imageEntity);
            documentService.delete(image);
        }

        return productEntity.getId();

    }

    @Override
    public Set<Product> findByCategory(Long categoryId, int offset, int limit){

        /**
         * 7
         * (index-1)*limit
         * oder by created_date: 7,6,5,4,3,2,1
         * index = 0, limit = 3 -> (7,6,5)
         * index = 1, limit = 3 -> (4,3,2)
         * index = 2, limit = 3 -> (1)
         */

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
}
