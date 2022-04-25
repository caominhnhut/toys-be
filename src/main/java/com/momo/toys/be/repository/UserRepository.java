package com.momo.toys.be.repository;

import com.momo.toys.be.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<UserEntity, Long>{
    List<UserEntity> findByEmail(String email);
    List<UserEntity> findAll();

   }
