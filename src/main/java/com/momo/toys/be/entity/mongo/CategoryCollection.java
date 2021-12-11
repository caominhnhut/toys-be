package com.momo.toys.be.entity.mongo;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;

@Document(value = "category")
public class CategoryCollection extends BaseCollection {

    @Field(value = "name")
    private String name;

    @Field(value = "products")
    private List<ProductCollection> products = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ProductCollection> getProducts() {
        return products;
    }
}
