package com.momo.toys.be.service;

import com.momo.toys.be.model.Product;
import javassist.NotFoundException;

import java.util.List;

public interface ProductService {

    String create(String categoryId, Product product) throws NotFoundException;

    List<Product> findByCategoryId(String categoryId) throws NotFoundException;
}
