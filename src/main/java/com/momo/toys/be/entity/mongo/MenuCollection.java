package com.momo.toys.be.entity.mongo;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "menu")
public class MenuCollection extends BaseCollection {

    @Field(value = "name")
    private String name;

    @DBRef
    List<CategoryCollection> categories = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CategoryCollection> getCategories() {
        return categories;
    }

}
