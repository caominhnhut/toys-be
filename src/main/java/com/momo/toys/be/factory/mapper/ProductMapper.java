package com.momo.toys.be.factory.mapper;

import java.util.function.Function;

import com.momo.toys.be.entity.ProductEntity;
import com.momo.toys.be.model.Product;

public class ProductMapper{

    public static final Function<Product, ProductEntity> mapModelToEntity = product -> {
        ProductEntity productEntity = new ProductEntity();
        productEntity.setName(product.getName());
        productEntity.setCode(product.getCode());
        productEntity.setAmount(product.getAmount());
        productEntity.setCostPrice(product.getCostPrice());
        productEntity.setPrice(product.getPrice());
        productEntity.setDescription(product.getDescription());
        productEntity.setMainImage(product.getMainImage());
        return productEntity;
    };

    public static final Function<com.momo.toys.be.product.Product, Product> mapDtoToModel = product -> {
        Product productModel = new Product();
        productModel.setName(product.getName());
        productModel.setCode(product.getCode());
        productModel.setAmount(product.getAmount());
        productModel.setCostPrice(product.getCostPrice());
        productModel.setPrice(product.getPrice());
        productModel.setDescription(product.getDescription());
        productModel.setMainImage(product.getMainImage());
        return productModel;
    };

    public static final Function<ProductEntity, Product> mapEntityToModel = productEntity -> {
        Product productModel = new Product();
        productModel.setName(productEntity.getName());
        productModel.setCode(productEntity.getCode());
        productModel.setAmount(productEntity.getAmount());
        productModel.setCostPrice(productEntity.getCostPrice());
        productModel.setPrice(productEntity.getPrice());
        productModel.setDescription(productEntity.getDescription());
        productModel.setMainImage(productEntity.getMainImage());
        productModel.setCreatedBy(productEntity.getCreatedBy());
        return productModel;
    };

    public static final Function<Product, com.momo.toys.be.product.Product> mapModelToDto = productModel -> {
        com.momo.toys.be.product.Product productDto = new com.momo.toys.be.product.Product();
        productDto.setName(productModel.getName());
        productDto.setCode(productModel.getCode());
        productDto.setAmount(productModel.getAmount());
        productDto.setCostPrice(productModel.getCostPrice());
        productDto.setPrice(productModel.getPrice());
        productDto.setDescription(productModel.getDescription());
        productDto.setMainImage(productModel.getMainImage());
        productDto.setCreatedBy(productModel.getCreatedBy());
        return productDto;
    };

    private ProductMapper(){
        // hide constructor
    }
}
