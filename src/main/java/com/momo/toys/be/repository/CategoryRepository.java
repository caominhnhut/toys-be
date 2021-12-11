package com.momo.toys.be.repository;

import com.momo.toys.be.entity.mongo.CategoryCollection;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CategoryRepository extends MongoRepository<CategoryCollection, Long> {
}
