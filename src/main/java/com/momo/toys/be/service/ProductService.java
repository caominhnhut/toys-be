package com.momo.toys.be.service;

import com.momo.toys.be.model.Product;

import javassist.NotFoundException;

public interface ProductService{

    Long create(Product product) throws NotFoundException;
}
