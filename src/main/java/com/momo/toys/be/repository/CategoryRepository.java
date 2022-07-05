package com.momo.toys.be.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.momo.toys.be.entity.CategoryEntity;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long>{

}
