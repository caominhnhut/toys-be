package com.momo.toys.be.controller;

import com.momo.toys.be.entity.mongo.ProductCollection;
import com.momo.toys.be.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/no-auth/products")
    public ResponseEntity findAll(){

        List<ProductCollection> products = productRepository.findAll();

        return ResponseEntity.status(200).body(products);
    }

    @PostMapping("/no-auth/products")
    public ResponseEntity create(@RequestBody ProductCollection productCollection){

        productRepository.save(productCollection);

        return ResponseEntity.status(200).body(productCollection);
    }
}
