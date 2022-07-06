package com.momo.toys.be.repository;

import java.math.BigDecimal;
import java.util.List;

import com.momo.toys.be.entity.ProductEntity;

public interface CustomProductRepository{

    List<ProductEntity> findByCriteria(String criteria);
    ProductEntity findProductById(Long id);
}
