package com.momo.toys.be.factory.mapper;

import com.momo.toys.be.dto.CategoryDto;
import com.momo.toys.be.entity.CategoryEntity;
import com.momo.toys.be.model.Category;
import net.bytebuddy.ByteBuddy;

import java.util.function.Function;

public class CategoryMapper {

    public static final Function<CategoryDto, Category> mapToModel = dto -> {
        Category model = new Category();
        model.setName(dto.getName());
        model.setNavigationId(dto.getNavigationId());

        return model;
    };

    public static final Function<Category, CategoryEntity> mapToEntity = category -> {
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setName(category.getName());

        return categoryEntity;

    };

    public static final Function<CategoryEntity, CategoryDto> mapToDto = categoryEntity -> {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(categoryEntity.getId());
        categoryDto.setName(categoryEntity.getName());

        return categoryDto;
    };


//    public static final Function<CategoryEntity, Category> mapEntityToModel = categoryEntity ->{
//        Category categoryModel = new Category();
//        categoryModel.setName(categoryEntity.getName());
//        categoryModel.setId(categoryEntity.getId());
//        return categoryModel;
//    };

    private CategoryMapper() {
    }
}