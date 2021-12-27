package com.momo.toys.be.repository;

import java.util.List;

import com.momo.toys.be.entity.mongo.MenuCollection;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface MenuRepository extends MongoRepository<MenuCollection, String> {

    @Query("{'status': ?0}")
    List<MenuCollection> findByStatus(String status);
}
