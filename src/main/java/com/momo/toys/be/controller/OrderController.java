package com.momo.toys.be.controller;

import com.momo.toys.be.account.Problem;
import com.momo.toys.be.dto.OrderDto;
import com.momo.toys.be.enumeration.SupportedType;
import com.momo.toys.be.exception.ValidationException;
import com.momo.toys.be.factory.CommonUtility;
import com.momo.toys.be.factory.mapper.OrderMapper;
import com.momo.toys.be.order.Order;
import com.momo.toys.be.service.OrderService;
import com.momo.toys.be.validation.ValidationData;
import com.momo.toys.be.validation.ValidationProvider;
import javassist.NotFoundException;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CommonUtility commonUtility;

    @Autowired
    private ValidationProvider validationProvider;

    private Function<List<Order>, Problem> validatorOrderCreation = order -> {
        Problem problem = new Problem();
        for (Order orders : order) {

            ValidationData validationData = new ValidationData().setQuantityProduct(orders.getQuantity());

            try {
                validationProvider.executeValidators(validationData, SupportedType.ORDER_CREATION);
            } catch (ValidationException e) {
                return commonUtility.createProblem(HttpStatus.INTERNAL_SERVER_ERROR.toString(), HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
            }
        }
        return problem;
    };

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_EMPLOYEE') OR hasRole('ROLE_CUSTOMER')")
    public ResponseEntity createOrder(@RequestBody List<Order> orders) throws NotFoundException, InterruptedException {

        Problem problem = validatorOrderCreation.apply(orders);
        if (Strings.isNotEmpty(problem.getTitle())) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problem);
        }

        List<com.momo.toys.be.model.Order> orderList = OrderMapper.mapDtotoModel.apply(orders);

        try {
            ResponseEntity<Long> orderId = orderService.create(orderList);
            return ResponseEntity.status(HttpStatus.OK).body(orderId);
        } catch (ValidationException e) {
            Problem problemOrder = commonUtility.createProblem(HttpStatus.INTERNAL_SERVER_ERROR.toString(), HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problemOrder);
        }
    }

    @PutMapping("/{order-id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_EMPLOYEE') OR hasRole('ROLE_CUSTOMER')")
    public ResponseEntity updateOrder(@PathVariable("order-id") Long orderId, @RequestBody List<Order> orders) throws NotFoundException, InterruptedException, ValidationException {

        List<com.momo.toys.be.model.Order> orderList = OrderMapper.mapDtotoModel.apply(orders);

        return ResponseEntity.status(HttpStatus.OK).body(orderService.update(orderList, orderId));
    }


    @DeleteMapping("/{order-id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_EMPLOYEE') OR hasRole('ROLE_CUSTOMER')")
    public ResponseEntity deleteOrder(@PathVariable("order-id") Long orderId) {

        try {
            orderService.removeProductOrderForUser(orderId);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body(true);
    }

    @GetMapping("/user/{user-id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_EMPLOYEE') OR hasRole('ROLE_CUSTOMER')")
    public ResponseEntity findOrderByUser(@PathVariable("user-id") Long userId) throws NotFoundException, ValidationException {

        return ResponseEntity.status(HttpStatus.OK).body(orderService.getOrderByUser(userId));
    }

    @GetMapping("/{from-date}/{to-date}")
    public ResponseEntity findOrderByDate(@PathVariable(value = "from-date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate, @PathVariable(value = "to-date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate) {

        /* fromDate : 2022-06-30  --> Expected: 2022-06-30 00:00:00
        toDate: 2022-07-06   --> ***Expected: 2022-07-(07) 00:00:00
        Db: 2022-07-06 21:12:24.161
         */
        List<OrderDto> orderDtos = orderService.getOrderbyDate(fromDate, toDate);

        if (orderDtos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Collections.emptyList());
        }
        return ResponseEntity.status(HttpStatus.OK).body(orderDtos);
    }
}


