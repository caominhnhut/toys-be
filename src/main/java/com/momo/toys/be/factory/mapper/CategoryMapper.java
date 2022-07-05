package com.momo.toys.be.factory.mapper;

import java.util.function.Function;

import com.momo.toys.be.dto.CategoryDTO;
import com.momo.toys.be.entity.CategoryEntity;
import com.momo.toys.be.model.Category;

public class CategoryMapper{

    private CategoryMapper(){
        // hide constructor
    }

    public static final Function<CategoryDTO, Category> mapToModel = dto -> {
        Category model = new Category();
        model.setName(dto.getName());
        return model;
    };

    public static final Function<Category, CategoryEntity> mapToEntity = model -> {
        CategoryEntity entity = new CategoryEntity();
        entity.setName(model.getName());
        return entity;
    };

    public static final Function<CategoryEntity, Category> mapFromEntity = entity -> {
        Category category = new Category();
        category.setId(entity.getId());
        category.setName(entity.getName());
        return category;
    };
}
