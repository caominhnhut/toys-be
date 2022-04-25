package com.momo.toys.be.service;

import com.momo.toys.be.dto.CategoryDto;
import com.momo.toys.be.entity.CategoryEntity;
import com.momo.toys.be.model.Category;
import com.momo.toys.be.model.Product;
import javassist.NotFoundException;

import java.util.List;
import java.util.Set;

public interface CategoryService {

    Set<Product> getAllProductsByCategory(Long categoryId) throws NotFoundException;

    Long create(Category category) throws NotFoundException;

    List<CategoryEntity> findAll() throws NotFoundException;

    List<CategoryEntity> findCategories(int offset, int limit);


    boolean updateCategory(CategoryDto categoryDto) throws NotFoundException;

    boolean deleteCategory(Long id) throws NotFoundException;
}
