package com.momo.toys.be.repository;

import com.momo.toys.be.entity.OrderEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface CustomOrderRepository {

    List<OrderEntity> findOrderByUser(Long userId);

    List<OrderEntity> findOrderByDate(Date fromDate, Date toDate);



}
