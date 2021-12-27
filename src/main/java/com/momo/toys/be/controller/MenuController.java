package com.momo.toys.be.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.momo.toys.be.service.MenuService;

@RestController
public class MenuController{

    @Autowired
    private MenuService menuService;

    @GetMapping("/no-auth/menus")
    public ResponseEntity findAll(){
        return ResponseEntity.status(HttpStatus.OK).body(menuService.findAll());
    }
}
