package com.momo.toys.be.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.momo.toys.be.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long>{
    List<UserEntity> findByEmail(String email);
}
