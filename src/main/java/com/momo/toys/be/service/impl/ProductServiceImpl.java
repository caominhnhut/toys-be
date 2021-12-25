package com.momo.toys.be.service.impl;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.momo.toys.be.entity.CategoryEntity;
import com.momo.toys.be.entity.DocumentEntity;
import com.momo.toys.be.entity.ProductEntity;
import com.momo.toys.be.factory.CommonUtility;
import com.momo.toys.be.factory.mapper.DocumentMapper;
import com.momo.toys.be.factory.mapper.ProductMapper;
import com.momo.toys.be.model.Document;
import com.momo.toys.be.model.Product;
import com.momo.toys.be.repository.CategoryRepository;
import com.momo.toys.be.repository.ProductRepository;
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

    @Override
    public Long create(Product product) throws NotFoundException{
        Optional<CategoryEntity> categoryEntityOptional = categoryRepository.findById(product.getCategoryId());
        if(!categoryEntityOptional.isPresent()){
            throw new NotFoundException(String.format("Category with id [%s] not found", product.getCategoryId()));
        }

        ProductEntity productEntity = ProductMapper.mapToEntity.apply(product);
        CategoryEntity categoryEntity = categoryEntityOptional.get();
        productEntity.setCategoryEntity(categoryEntity);

        Set<DocumentEntity> imagesEntity = new HashSet<>();
        for(Document image : product.getImages()){
            Document imageModel = documentService.upload(image);
            DocumentEntity imageEntity = DocumentMapper.mapToDocumentEntity.apply(imageModel);
            imagesEntity.add(imageEntity);
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        productEntity.setImages(imagesEntity);
        productEntity.setCreatedBy(authentication.getName());
        ProductEntity createdProduct = productRepository.save(productEntity);
        return createdProduct.getId();
    }
}
