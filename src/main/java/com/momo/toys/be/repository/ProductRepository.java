package com.momo.toys.be.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.momo.toys.be.entity.ProductEntity;
import com.momo.toys.be.enumeration.EntityStatus;

public interface ProductRepository extends JpaRepository<ProductEntity, Long>, CustomProductRepository{

    @Query("Select p from ProductEntity as p where p.categoryEntity.id = :categoryId and p.status != :notEntityStatus")
    Page<ProductEntity> findByCategory(Long categoryId, Pageable pageable, EntityStatus notEntityStatus);
}
