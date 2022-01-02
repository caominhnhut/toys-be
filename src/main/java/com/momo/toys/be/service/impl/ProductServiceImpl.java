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
import com.momo.toys.be.entity.ProductEntity;
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
