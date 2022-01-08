package com.momo.toys.be.service.impl;

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

        /*
        stream
        loop product.getImages() -> element{
            string originalName = element.getName
            string uniname = buildUniqueName(originalName)
            element.setName(uniname)
            if(originalName == product.getMainName){
                product.setMainName(uniname)
            }
        }

         */

        ProductEntity productEntity = ProductMapper.mapModelToEntity.apply(product);
        productEntity.setCategoryEntity(categoryEntityOptional.get());
        productEntity.setCreatedBy(accountService.getAuthorizedAccount().getName());
        productRepository.save(productEntity);

        //TODO: stream
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
        // TODO: update this one
        productEntity.setMainImage(product.getMainImage());
        productRepository.save(productEntity);

        List<Document> images = product.getImages();
        if(images.isEmpty()){
            return productEntity.getId();
        }

//        // Delete existing images
//        Set<DocumentEntity> existingImages = productEntity.getImages();
//        for(DocumentEntity imageEntity : existingImages){
//            Document image = DocumentMapper.mapEntityToDocument.apply(imageEntity);
//            documentService.delete(image);
//        }
//
//        // Upload new images
//        for(Document image : images){
//            documentService.upload(image, productEntity);
//        }

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
