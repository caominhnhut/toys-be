package com.momo.toys.be.repository;

import com.momo.toys.be.entity.ProductEntity;
import com.momo.toys.be.entity.UserEntity;

import java.util.List;

public interface CustomUserRepository {
    List<UserEntity> findByStatus(String status);
}

