package com.momo.toys.be.service;

import com.momo.toys.be.model.Product;
import javassist.NotFoundException;

public interface ProductService {

    String create(String categoryId, Product product) throws NotFoundException;
}
