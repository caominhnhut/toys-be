package com.momo.toys.be.controller;

import static com.momo.toys.be.enumeration.SupportedType.DOCUMENT_UPLOADING;
import static com.momo.toys.be.enumeration.SupportedType.PRODUCT_CREATION;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.momo.toys.be.account.Problem;
import com.momo.toys.be.exception.ValidationException;
import com.momo.toys.be.factory.CommonUtility;
import com.momo.toys.be.factory.mapper.DocumentMapper;
import com.momo.toys.be.factory.mapper.ProductMapper;
import com.momo.toys.be.model.Document;
import com.momo.toys.be.product.Image;
import com.momo.toys.be.product.Product;
import com.momo.toys.be.service.CategoryService;
import com.momo.toys.be.service.DocumentService;
import com.momo.toys.be.service.ProductService;
import com.momo.toys.be.validation.ValidationData;
import com.momo.toys.be.validation.ValidationProvider;

import javassist.NotFoundException;

@RestController
public class ProductController{

    @Autowired
    private ValidationProvider validationProvider;

    @Autowired
    private CommonUtility commonUtility;

    @Autowired
    private ProductService productService;

    @Autowired
    private DocumentService documentService;

    @Autowired
    private CategoryService categoryService;

    private Function<com.momo.toys.be.model.Product, Product> buildFromModel = productModel -> {

        Product product = ProductMapper.mapModelToDto.apply(productModel);

        if(productModel.getImages() != null && !productModel.getImages().isEmpty()){

            List<Image> images = productModel.getImages().stream().map(DocumentMapper.mapModelToDto).collect(Collectors.toList());
            product.setImages(images);
        }

        return product;
    };

    private Function<Product, Problem> validatorProductCreating = product -> {

        Problem problem = new Problem();

        ValidationData validationData = new ValidationData();
        validationData.setProductName(product.getName());
        validationData.setProductCode(product.getCode());
        try{
            validationProvider.executeValidators(validationData, PRODUCT_CREATION);
        }catch(ValidationException e){
            return commonUtility.createProblem(HttpStatus.INTERNAL_SERVER_ERROR.toString(), HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }

        return problem;
    };

    private Function<MultipartFile, Problem> validatorDocument = multipartFile -> {

        ValidationData validationData = new ValidationData().setMultipartFile(multipartFile);

        try{
            validationProvider.executeValidators(validationData, DOCUMENT_UPLOADING);
        }catch(ValidationException e){
            return commonUtility.createProblem(HttpStatus.INTERNAL_SERVER_ERROR.toString(), HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }

        return new Problem();
    };

    @PostMapping("/categories/{category-id}/products")
    @PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_EMPLOYEE')")
    public ResponseEntity create(@PathVariable("category-id") Long categoryId, @RequestPart("product") Product product, @RequestPart(value = "files", required = false) List<MultipartFile> files){

        Problem problem = validatorProductCreating.apply(product);
        if(Strings.isNotEmpty(problem.getTitle())){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problem);
        }

        com.momo.toys.be.model.Product productModel = ProductMapper.mapDtoToModel.apply(product);

        if(files != null){
            for(MultipartFile file : files){
                problem = validatorDocument.apply(file);
                if(Strings.isNotEmpty(problem.getTitle())){
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problem);
                }
            }

            List<Document> images = files.stream().map(DocumentMapper.mapToDocument).collect(Collectors.toList());
            productModel.setImages(images);
        }

        productModel.setCategoryId(categoryId);

        try{
            Long productId = productService.create(productModel);
            return ResponseEntity.status(HttpStatus.CREATED).body(productId);
        }catch(Exception e){
            Problem problemCategoryNotFound = commonUtility.createProblem(HttpStatus.INTERNAL_SERVER_ERROR.toString(), HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problemCategoryNotFound);
        }
    }

    @GetMapping("/no-auth/categories/{category-id}/products")
    public ResponseEntity findByCategory(@PathVariable("category-id") Long categoryId, @RequestParam("offset") int offset, @RequestParam("limit") int limit){

        Set<com.momo.toys.be.model.Product> productModels = productService.findByCategory(categoryId, offset, limit);

        if(productModels.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Collections.emptySet());
        }

        Set<Product> products = productModels.stream().map(buildFromModel).collect(Collectors.toSet());

        return ResponseEntity.status(HttpStatus.OK).body(products);
    }

    @PutMapping("/categories/{category-id}/products/{product-id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_EMPLOYEE')")
    public ResponseEntity update(@PathVariable("category-id") Long categoryId, @PathVariable("product-id") Long productId, @RequestPart("product") Product product, @RequestPart(value = "files", required = false) List<MultipartFile> files){

        Problem problem = validatorProductCreating.apply(product);
        if(Strings.isNotEmpty(problem.getTitle())){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problem);
        }

        com.momo.toys.be.model.Product productModel = ProductMapper.mapDtoToModel.apply(product);

        if(files != null){
            for(MultipartFile file : files){
                problem = validatorDocument.apply(file);
                if(Strings.isNotEmpty(problem.getTitle())){
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problem);
                }
            }

            List<Document> images = files.stream().map(DocumentMapper.mapToDocument).collect(Collectors.toList());
            productModel.setImages(images);
        }

        productModel.setCategoryId(categoryId);
        productModel.setId(productId);

        try{
            productService.update(productModel);
            return ResponseEntity.status(HttpStatus.OK).body(productId);
        }catch(Exception e){
            Problem problemProductNotFound = commonUtility.createProblem(HttpStatus.INTERNAL_SERVER_ERROR.toString(), HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problemProductNotFound);
        }
    }

    @DeleteMapping("/products/{product-id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_EMPLOYEE')")
    public ResponseEntity deleteProduct(@PathVariable("product-id") Long productId, @RequestParam(name = "is-soft-delete", required = false, defaultValue = "false") Boolean isSoftDelete){

        try{
            productService.delete(productId, isSoftDelete);
        }catch(NotFoundException e){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(e.getMessage());
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.OK).body(true);
    }

    @GetMapping("/no-auth/products")
    public ResponseEntity findByCriteria(@RequestParam("criteria") String criteria){

        List<com.momo.toys.be.model.Product> productModels = productService.findByCriteria(criteria);

        if(productModels.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Collections.emptySet());
        }

        Set<Product> products = productModels.stream().map(buildFromModel).collect(Collectors.toSet());

        return ResponseEntity.status(HttpStatus.OK).body(products);
    }


}
