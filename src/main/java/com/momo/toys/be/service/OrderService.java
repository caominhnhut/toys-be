package com.momo.toys.be.service;

import com.momo.toys.be.dto.OrderDto;
import com.momo.toys.be.entity.ProductEntity;
import com.momo.toys.be.exception.ValidationException;
import com.momo.toys.be.model.Order;
import javassist.NotFoundException;
import org.springframework.http.ResponseEntity;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public interface OrderService {

    ResponseEntity create(List<Order> orders) throws NotFoundException, InterruptedException, ValidationException;

    Long update(List<Order> orders, Long orderId) throws NotFoundException, InterruptedException, ValidationException;

    Boolean removeProductOrderForUser(Long orderId) throws NotFoundException, InterruptedException;

    List<ProductEntity> getAllProductsByOrder(Long orderId) throws NotFoundException;

    List<OrderDto> getOrderByUser(Long userId) throws NotFoundException, ValidationException;

    List<OrderDto> getOrderbyDate(Date fromDate, Date toDate) ;
}
