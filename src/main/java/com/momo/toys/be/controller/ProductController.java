package com.momo.toys.be.controller;

import com.momo.toys.be.product.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import com.momo.toys.be.validation.ValidationProvider;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class ProductController {

    @Autowired
    private ValidationProvider validationProvider;

    @PostMapping
    @PreAuthorize()
    public ResponseEntity create(@RequestPart("product") Product product, @RequestPart("files") List<MultipartFile> files) {

        return null;
    }

}
