package com.momo.toys.be.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.momo.toys.be.validation.ValidationProvider;

@RestController
public class ProductController{
    @Autowired
    private ValidationProvider validationProvider;
}
