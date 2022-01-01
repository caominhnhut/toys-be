package com.momo.toys.be.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.momo.toys.be.entity.CategoryEntity;
import com.momo.toys.be.entity.ProductEntity;
import com.momo.toys.be.factory.CommonUtility;
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

        ProductEntity productEntity = ProductMapper.mapModelToEntity.apply(product);
        productEntity.setCategoryEntity(categoryEntityOptional.get());
        productEntity.setCreatedBy(accountService.getAuthorizedAccount().getName());
        productRepository.save(productEntity);

        for(Document image : product.getImages()){
            documentService.upload(image, productEntity);
        }

        return productEntity.getId();
    }
}
