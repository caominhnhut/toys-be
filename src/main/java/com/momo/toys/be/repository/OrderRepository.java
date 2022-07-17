package com.momo.toys.be.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.momo.toys.be.entity.OrderEntity;

public interface OrderRepository extends JpaRepository<OrderEntity, Long>{

}
