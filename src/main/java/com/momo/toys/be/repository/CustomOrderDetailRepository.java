package com.momo.toys.be.repository;

import com.momo.toys.be.entity.OrderDetailEntity;

import java.util.List;

public interface CustomOrderDetailRepository {
    List<OrderDetailEntity> findOrderDetailByOrder(Long Id);
}
