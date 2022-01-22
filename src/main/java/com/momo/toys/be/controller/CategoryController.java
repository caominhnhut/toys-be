package com.momo.toys.be.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.momo.toys.be.enumeration.EntityStatus;
import com.momo.toys.be.service.CategoryService;

@RestController
public class CategoryController{

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/no-auth/categories")
    public ResponseEntity findByStatus(@RequestParam("status") String status){

        return ResponseEntity.status(HttpStatus.OK).body(categoryService.findByStatus(EntityStatus.valueOf(status)));

    }
}
