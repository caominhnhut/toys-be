package com.momo.toys.be.service.impl;

import com.momo.toys.be.dto.CategoryDto;
import com.momo.toys.be.entity.CategoryEntity;
import com.momo.toys.be.entity.NavigationEntity;
import com.momo.toys.be.entity.ProductEntity;
import com.momo.toys.be.enumeration.EntityStatus;
import com.momo.toys.be.factory.mapper.CategoryMapper;
import com.momo.toys.be.factory.mapper.DocumentMapper;
import com.momo.toys.be.factory.mapper.ProductMapper;
import com.momo.toys.be.model.Category;
import com.momo.toys.be.model.Document;
import com.momo.toys.be.model.Product;
import com.momo.toys.be.repository.CategoryRepository;
import com.momo.toys.be.service.CategoryService;
import com.momo.toys.be.service.NavigationService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private NavigationService navigationService;

    @Override
    public Set<Product> getAllProductsByCategory(Long categoryId) throws NotFoundException {

        Optional<CategoryEntity> categoryEntityOptional = categoryRepository.findById(categoryId);

        if (!categoryEntityOptional.isPresent()) {
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

    @Override
    public Long create(Category category) throws NotFoundException {

        CategoryEntity categoryEntity = CategoryMapper.mapToEntity.apply(category);

        NavigationEntity navigationEntity = navigationService.findById(category.getNavigationId());
        categoryEntity.setNavigation(navigationEntity);

        CategoryEntity createdCategory = categoryRepository.save(categoryEntity);

        return createdCategory.getId();
    }

    @Override
    public List<CategoryEntity> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public List<CategoryEntity> findCategories(int offset, int limit) {

        Page<CategoryEntity> categoriesEntity = categoryRepository.findAll(PageRequest.of(offset, limit));
        List<CategoryEntity> categoryEntities = categoriesEntity.getContent();
        return categoryEntities;
    }

    @Override
    public boolean updateCategory(CategoryDto categoryDto) throws NotFoundException {
        Optional<CategoryEntity> categoryEntityOptional = categoryRepository.findById(categoryDto.getId());
        if (categoryEntityOptional.isPresent()) {
            CategoryEntity categoryEntity = categoryEntityOptional.get();
            categoryEntity.setName(categoryDto.getName());
            categoryEntity.setStatus(EntityStatus.valueOf(categoryDto.getStatus()));

            NavigationEntity navigationEntity = navigationService.findById(categoryDto.getNavigationId());
            categoryEntity.setNavigation(navigationEntity);

            categoryRepository.save(categoryEntity);
            return true;
        }
        throw new NotFoundException(String.format("Category with id [%s] not found", categoryDto.getId()));
    }

    @Override
    public boolean deleteCategory(Long id) throws NotFoundException{
        categoryRepository.deleteById(id);
        return true;

    }
}
