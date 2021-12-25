package com.momo.toys.be.factory.mapper;

import java.util.function.Function;

import com.momo.toys.be.entity.CategoryEntity;
import com.momo.toys.be.entity.NavigationEntity;
import com.momo.toys.be.entity.ProductEntity;
import com.momo.toys.be.model.Navigation;
import com.momo.toys.be.model.Product;

public class ProductMapper{
    private ProductMapper(){
        // hide constructor
    }
    public static final Function<Product, ProductEntity> mapToEntity = product -> {
        ProductEntity productEntity = new ProductEntity();
        productEntity.setName(product.getName());
        productEntity.setCode(product.getCode());
        productEntity.setAmount(product.getAmount());
        productEntity.setCostPrice(product.getCostPrice());
        productEntity.setPrice(product.getPrice());
        productEntity.setDescription(product.getDescription());
        productEntity.setMainImage(product.getMainImage());
        return null;
    };

    public static final Function<com.momo.toys.be.product.Product, Product> mapToModel = product -> {
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
}
