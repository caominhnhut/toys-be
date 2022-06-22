package com.momo.toys.be.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.momo.toys.be.service.OrderService;

@RestController
@RequestMapping("/orders")
public class OrderController{

    @Autowired
    private OrderService orderService;

    @PostMapping("/{order-id}/checkout")
    public String checkOut(@PathVariable("order-id") String orderId){
        return orderService.checkOut(orderId);
    }
}
