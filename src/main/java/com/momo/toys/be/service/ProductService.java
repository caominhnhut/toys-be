package com.momo.toys.be.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.momo.toys.be.entity.ProductEntity;
import com.momo.toys.be.exception.FileStorageException;
import com.momo.toys.be.model.Product;

import javassist.NotFoundException;

public interface ProductService{

    Long create(Product product) throws NotFoundException, InterruptedException;

    Long update(Product product) throws NotFoundException, FileStorageException;

    Set<Product> findByCategory(Long categoryId, int offset, int limit);

    Boolean delete(Long productId, boolean isSoftDelete) throws NotFoundException;

    List<Product> findByCriteria(String criteria);

    ProductEntity findProductById(Long productId);
}
