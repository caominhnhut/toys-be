package com.momo.toys.be.repository;

import com.momo.toys.be.entity.OrderDetailEntity;
import com.momo.toys.be.entity.ProductEntity;
import com.momo.toys.be.enumeration.EntityStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Calendar;
import java.util.List;

public interface OrderDetailRepository extends CrudRepository<OrderDetailEntity, Long>, CustomOrderDetailRepository {



}

