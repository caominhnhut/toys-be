package com.momo.toys.be.repository;

import com.momo.toys.be.entity.CategoryEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface CategoryRepository extends CrudRepository<CategoryEntity, Long>, PagingAndSortingRepository<CategoryEntity, Long> {
    List<CategoryEntity> findAll();


}
