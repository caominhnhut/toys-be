package com.momo.toys.be.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.momo.toys.be.entity.mongo.CategoryCollection;
import com.momo.toys.be.enumeration.EntityStatus;
import com.momo.toys.be.factory.mapper.CategoryMapper;
import com.momo.toys.be.model.Category;
import com.momo.toys.be.repository.CategoryRepository;
import com.momo.toys.be.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService{

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<Category> findByStatus(EntityStatus entityStatus){

        List<CategoryCollection> categoriesCollection = categoryRepository.findByStatus(entityStatus.toString());

        return categoriesCollection.stream().map(CategoryMapper.mapFromCollection).collect(Collectors.toList());
    }
}
