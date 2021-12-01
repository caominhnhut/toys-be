package com.momo.toys.be.repository;

import org.springframework.data.repository.CrudRepository;

import com.momo.toys.be.entity.UserEntity;

public interface UserRepository extends CrudRepository<UserEntity, Long>{

}
