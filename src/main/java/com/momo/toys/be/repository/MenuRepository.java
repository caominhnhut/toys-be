package com.momo.toys.be.repository;

import com.momo.toys.be.entity.mongo.MenuCollection;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MenuRepository extends MongoRepository<MenuCollection, String> {
}
