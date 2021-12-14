package com.momo.toys.be.repository;

import com.momo.toys.be.entity.mongo.ProductCollection;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<ProductCollection, String> {

}
