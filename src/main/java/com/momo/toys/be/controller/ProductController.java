package com.momo.toys.be.controller;

import static com.momo.toys.be.enumeration.SupportedType.DOCUMENT_UPLOADING;
import static com.momo.toys.be.enumeration.SupportedType.PRODUCT_CREATION;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

    @PostMapping("/categories/{category-id}/products")
    @PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_EMPLOYEE')")
    public ResponseEntity create(@PathVariable("category-id") Long categoryId, @RequestPart("product") Product product, @RequestPart("files") List<MultipartFile> files){

        Problem problem = validatorProductCreating.apply(product);
        if(Strings.isNotEmpty(problem.getTitle())){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problem);
        }

        for(MultipartFile file : files){
            problem = validatorDocument.apply(file);
            if(Strings.isNotEmpty(problem.getTitle())){
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problem);
            }
        }

        List<Document> images = new ArrayList<>();
        for(MultipartFile multipartFile : files){
            images.add(DocumentMapper.mapToDocument.apply(multipartFile));
        }

        com.momo.toys.be.model.Product productModel = ProductMapper.mapDtoToModel.apply(product);
        productModel.setImages(images);
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
    public ResponseEntity getAllByCategory(@PathVariable("category-id") Long categoryId){

        Set<com.momo.toys.be.model.Product> productModels;
        try{
            productModels = categoryService.getAllProductsByCategory(categoryId);
        }catch(Exception e){
            Problem problemCategoryNotFound = commonUtility.createProblem(HttpStatus.INTERNAL_SERVER_ERROR.toString(), HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problemCategoryNotFound);
        }

        Set<Product> products = productModels.stream().map(buildFromModel).collect(Collectors.toSet());

        return ResponseEntity.status(HttpStatus.OK).body(products);
    }

    private Function<com.momo.toys.be.model.Product, Product> buildFromModel = productModel -> {

        Product product = ProductMapper.mapModelToDto.apply(productModel);

        List<Image> images = productModel.getImages().stream().map(imageModel -> DocumentMapper.mapModelToDto.apply(imageModel)).collect(Collectors.toList());

        product.setImages(images);

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
}
