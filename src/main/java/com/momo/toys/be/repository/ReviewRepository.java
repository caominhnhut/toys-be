package com.momo.toys.be.repository;

import com.momo.toys.be.entity.ReviewEntity;
import org.springframework.data.repository.CrudRepository;

public interface ReviewRepository extends CrudRepository<ReviewEntity, Long>, CustomProductRepository{

}
