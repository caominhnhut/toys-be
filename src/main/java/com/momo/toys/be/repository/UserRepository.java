package com.momo.toys.be.repository;

import org.springframework.data.repository.CrudRepository;

import com.momo.toys.be.entity.UserEntity;

import java.util.List;

public interface UserRepository extends CrudRepository<UserEntity, Long>{
    List<UserEntity> findByEmail(String email);
}
