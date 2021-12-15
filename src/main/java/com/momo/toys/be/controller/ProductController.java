package com.momo.toys.be.controller;

import com.momo.toys.be.account.Problem;
import com.momo.toys.be.factory.CommonUtility;
import com.momo.toys.be.factory.mapper.ProductMapper;
import com.momo.toys.be.product.Product;
import com.momo.toys.be.product.ProductId;
import com.momo.toys.be.service.ProductService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RestController()
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CommonUtility commonUtility;

    @GetMapping("/no-auth/category/{category-id}")
    public ResponseEntity findProductsByCategory(@PathVariable("category-id") String categoryId) {

        List<com.momo.toys.be.model.Product> productsModel = null;
        try {
            productsModel = productService.findByCategoryId(categoryId);
        } catch (NotFoundException e) {
            Problem problem = commonUtility.createProblem(HttpStatus.INTERNAL_SERVER_ERROR.toString(), HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problem);
        }

        List<Product> products = productsModel.stream().map(ProductMapper.mapToProductDto::apply).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(products);
    }

    @PostMapping(value = "/category/{category-id}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE')")
    public ResponseEntity create(@RequestPart("product") Product product, @RequestPart("images") MultipartFile[] images, @PathVariable("category-id") String categoryId) {

        com.momo.toys.be.model.Product productModel = ProductMapper.mapToProductModel.apply(product);

        ProductMapper.mapImages.accept(images, productModel);

        String id;
        try {
            id = productService.create(categoryId, productModel);
        } catch (NotFoundException e) {
            Problem problem = commonUtility.createProblem(HttpStatus.INTERNAL_SERVER_ERROR.toString(), HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problem);
        }

        ProductId productId = new ProductId();
        productId.setId(id);

        return ResponseEntity.status(HttpStatus.CREATED).body(productId);
    }
}
