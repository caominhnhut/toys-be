package com.momo.toys.be.repository;

import java.util.List;

import com.momo.toys.be.entity.mongo.CategoryCollection;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface CategoryRepository extends MongoRepository<CategoryCollection, String> {

    @Query("{'status': ?0}")
    List<CategoryCollection> findByStatus(String status);
}
