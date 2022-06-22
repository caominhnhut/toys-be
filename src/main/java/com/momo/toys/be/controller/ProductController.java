package com.momo.toys.be.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.momo.toys.be.dto.Problem;
import com.momo.toys.be.factory.CommonUtility;
import com.momo.toys.be.factory.JsonHelper;
import com.momo.toys.be.factory.mapper.ProductMapper;
import com.momo.toys.be.product.Product;
import com.momo.toys.be.product.ProductId;
import com.momo.toys.be.service.ProductService;

import javassist.NotFoundException;

@RestController
public class ProductController{

    @Autowired
    private ProductService productService;

    @Autowired
    private CommonUtility commonUtility;

    @Autowired
    private JsonHelper jsonHelper;

    @GetMapping("/no-auth/category/{category-id}")
    public ResponseEntity findProductsByCategory(@PathVariable("category-id") String categoryId){

        List<com.momo.toys.be.model.Product> productsModel = null;
        try{
            productsModel = productService.findByCategoryId(categoryId);
        }catch(NotFoundException e){
            com.momo.toys.be.dto.Problem problem = commonUtility.createProblem(HttpStatus.INTERNAL_SERVER_ERROR.toString(), HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problem);
        }

        List<Product> products = productsModel.stream().map(ProductMapper.mapToProductDto).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(products);
    }

    @PostMapping(value = "/categories/{category-id}/products")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE')")
    public ResponseEntity create(@RequestParam("product") String productJson, @RequestParam(value = "images", required = false) MultipartFile[] images, @PathVariable("category-id") String categoryId){

        Product product = jsonHelper.convertJsonFromString(productJson, Product.class);
        com.momo.toys.be.model.Product productModel = ProductMapper.mapToProductModel.apply(product);

        if(images != null){
            ProductMapper.mapImages.accept(images, productModel);
        }

        try{
            ProductId productId = new ProductId();
            productId.setId(productService.create(categoryId, productModel));
            return ResponseEntity.status(HttpStatus.CREATED).body(productId);
        }catch(NotFoundException e){
            Problem problem = commonUtility.createInternalError(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problem);
        }
    }
}
