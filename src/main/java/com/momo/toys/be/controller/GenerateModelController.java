package com.momo.toys.be.controller;

import com.momo.toys.be.enumeration.ModelType;
import com.momo.toys.be.product.Product;
import com.momo.toys.be.service.GenerateModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GenerateModelController {

    @Autowired
    private GenerateModelService generateModelService;

    @GetMapping("/no-auth/generate/{model-type}")
    public ResponseEntity generate(@PathVariable("model-type") ModelType modelType) {
        //ModelType modelType = ModelType.valueOf(type);
        switch (modelType) {
            case PRODUCT:
                Product product = generateModelService.generateProduct();
                return ResponseEntity.status(HttpStatus.OK).body(product);
            default:
                break;
        }

        return null;
    }
}
