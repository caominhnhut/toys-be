package com.momo.toys.be.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.momo.toys.be.account.Problem;
import com.momo.toys.be.dto.ShoppingCartDTO;
import com.momo.toys.be.factory.CommonUtility;
import com.momo.toys.be.factory.mapper.OrderMapper;
import com.momo.toys.be.model.ShoppingCart;
import com.momo.toys.be.service.OrderService;

@RestController
@RequestMapping("/orders")
public class OrderController{

    @Autowired
    private OrderService orderService;

    @Autowired
    private CommonUtility commonUtility;

    @PostMapping
    public ResponseEntity create(@RequestBody ShoppingCartDTO shoppingCart){

        // TODO: do the validation latter
        ShoppingCart shoppingCartModel = OrderMapper.mapFromDTO.apply(shoppingCart);

        try{
            Long productId = orderService.create(shoppingCartModel);
            return ResponseEntity.status(HttpStatus.CREATED).body(productId);
        }catch(Exception e){
            Problem problemCategoryNotFound = commonUtility.createProblem(HttpStatus.INTERNAL_SERVER_ERROR.toString(), HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problemCategoryNotFound);
        }
    }
}
