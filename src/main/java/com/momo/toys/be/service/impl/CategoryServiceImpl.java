package com.momo.toys.be.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.momo.toys.be.entity.CategoryEntity;
import com.momo.toys.be.entity.NavigationEntity;
import com.momo.toys.be.entity.ProductEntity;
import com.momo.toys.be.factory.mapper.CategoryMapper;
import com.momo.toys.be.factory.mapper.DocumentMapper;
import com.momo.toys.be.factory.mapper.ProductMapper;
import com.momo.toys.be.model.Category;
import com.momo.toys.be.model.Document;
import com.momo.toys.be.model.Product;
import com.momo.toys.be.repository.CategoryRepository;
import com.momo.toys.be.repository.NavigationRepository;
import com.momo.toys.be.service.CategoryService;

import javassist.NotFoundException;

@Service
public class CategoryServiceImpl implements CategoryService{

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private NavigationRepository navigationRepository;

    @Override
    public Set<Product> getAllProductsByCategory(Long categoryId) throws NotFoundException{

        Optional<CategoryEntity> categoryEntityOptional = categoryRepository.findById(categoryId);

        if(!categoryEntityOptional.isPresent()){
            throw new NotFoundException(String.format("The category with id [%s] not found", categoryId));
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

    @Override
    public Long create(Category category) throws NotFoundException{

        Optional<NavigationEntity> optNavigation = navigationRepository.findById(category.getNavigationId());
        if(!optNavigation.isPresent()){
            throw new NotFoundException(String.format("The navigation with id [%s] not found", category.getNavigationId()));
        }

        CategoryEntity entity = CategoryMapper.mapToEntity.apply(category);
        entity.setNavigation(optNavigation.get());
        categoryRepository.save(entity);
        return entity.getId();
    }

    @Override
    public List<Category> getCategoryByNavigation(Long navigationId){

        NavigationEntity navigation = new NavigationEntity();
        navigation.setId(navigationId);
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setNavigation(new NavigationEntity());
        Example<CategoryEntity> example = Example.of(categoryEntity);

        List<CategoryEntity> categoryEntities = categoryRepository.findAll(example);
        return categoryEntities.stream().map(CategoryMapper.mapFromEntity).collect(Collectors.toList());
    }
}
