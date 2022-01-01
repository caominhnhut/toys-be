package com.momo.toys.be.service;

import java.util.Set;

import com.momo.toys.be.model.Product;

import javassist.NotFoundException;

public interface CategoryService{

    Set<Product> getAllProductsByCategory(Long categoryId) throws NotFoundException;
}
