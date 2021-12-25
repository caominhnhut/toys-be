package com.momo.toys.be.repository;

import org.springframework.data.repository.CrudRepository;

import com.momo.toys.be.entity.ProductEntity;

public interface ProductRepository extends CrudRepository<ProductEntity, Long>{

}
