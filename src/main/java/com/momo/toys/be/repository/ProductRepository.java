package com.momo.toys.be.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.momo.toys.be.entity.ProductEntity;
import com.momo.toys.be.enumeration.EntityStatus;

public interface ProductRepository extends JpaRepository<ProductEntity, Long>, CustomProductRepository{

    @Query("Select p from ProductEntity as p where p.categoryEntity.id = :categoryId and p.status != :notEntityStatus")
    Page<ProductEntity> findByCategory(Long categoryId, Pageable pageable, EntityStatus notEntityStatus);

    @Query("Select p from ProductEntity as p where p.id in :productIds and p.status = :status")
    Collection<ProductEntity> findByIDs(List<Long> productIds, EntityStatus status);
}
