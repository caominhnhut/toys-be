package com.momo.toys.be.factory.mapper;

import java.util.function.Function;

import com.momo.toys.be.entity.mongo.CategoryCollection;
import com.momo.toys.be.model.Category;

public class CategoryMapper{

    private CategoryMapper(){
        // hidden constructor
    }

    public static final Function<CategoryCollection, Category> mapFromCollection = category -> new Category(category.getId(), category.getName());
}
