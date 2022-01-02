package com.momo.toys.be.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.momo.toys.be.entity.ProductEntity;

public interface ProductRepository extends CrudRepository<ProductEntity, Long>{

    @Query("Select p from ProductEntity as p where p.categoryEntity.id = :categoryId")
    Page<ProductEntity> findByCategory(Long categoryId, Pageable pageable);
}
