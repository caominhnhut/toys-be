package com.momo.toys.be.factory.mapper;

import com.momo.toys.be.dto.CategoryDto;
import com.momo.toys.be.entity.CategoryEntity;
import com.momo.toys.be.model.Category;

import java.util.function.Function;

public class CategoryMapper {

    public static final Function<CategoryDto, Category> mapToModel = dto -> {
        Category model = new Category();
        model.setName(dto.getName());
        return model;
    };

    public static final Function<Category, CategoryEntity> maptoEntity = category -> {
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setName(category.getName());

        return categoryEntity;

    };

    private CategoryMapper() {
    }
}