package com.momo.toys.be.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.momo.toys.be.entity.CategoryEntity;
import com.momo.toys.be.entity.ProductEntity;
import com.momo.toys.be.factory.mapper.DocumentMapper;
import com.momo.toys.be.factory.mapper.ProductMapper;
import com.momo.toys.be.model.Document;
import com.momo.toys.be.model.Product;
import com.momo.toys.be.repository.CategoryRepository;
import com.momo.toys.be.service.CategoryService;

import javassist.NotFoundException;

@Service
public class CategoryServiceImpl implements CategoryService{

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Set<Product> getAllProductsByCategory(Long categoryId) throws NotFoundException{

        Optional<CategoryEntity> categoryEntityOptional = categoryRepository.findById(categoryId);

        if(!categoryEntityOptional.isPresent()){
            throw new NotFoundException(String.format("Category with id [%s] not found", categoryId));
        }

        CategoryEntity categoryEntity = categoryEntityOptional.get();
        Set<ProductEntity> productEntities = categoryEntity.getProductEntities();

        return productEntities.stream().map(productEntity -> {
            Product productModel = ProductMapper.mapEntityToModel.apply(productEntity);
            List<Document> documents = productEntity.getImages().stream().map(DocumentMapper.mapEntityToDocument).collect(Collectors.toList());
            productModel.setImages(documents);
            return productModel;
        }).collect(Collectors.toSet());
    }
}
