package com.momo.toys.be.service;

import com.momo.toys.be.model.ShoppingCart;

import javassist.NotFoundException;

public interface OrderService{

    Long create(ShoppingCart shoppingCart) throws NotFoundException;
}
