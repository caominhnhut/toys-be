package com.momo.toys.be.repository;

import com.momo.toys.be.entity.OrderDetailEntity;
import com.momo.toys.be.entity.OrderEntity;
import com.momo.toys.be.entity.ProductEntity;
import com.momo.toys.be.enumeration.EntityStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public interface OrderRepository extends CrudRepository<OrderEntity, Long>, CustomOrderRepository {

//    @Query(nativeQuery = true, value = "select c.cons_no, c.pick_date, from OrderEntity c where c.categoryEntity.id = :categoryId and c.pick_date between :startDate and :endDate")
//    List<OrderEntity> getDateBetween(Long id,@Param("startDate") Calendar date, @Param("endDate") Calendar date2);

    @Query("select a from OrderEntity a where a.createdDate <= :createdDate")
    List<OrderEntity> findAllWithCreationDateTimeBefore(@Param("createdDate") Date createdDate);
}

