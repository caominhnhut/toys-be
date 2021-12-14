package com.momo.toys.be.model;

import com.momo.toys.be.enumeration.EnumColor;
import com.momo.toys.be.enumeration.EnumTag;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Product {

    private String id;

    private String code;

    private String name;

    private String owner;

    private String description;

    private String imageName;

    private int amount;

    private BigDecimal costPrice;

    private BigDecimal price;

    private EnumColor color;

    private List<EnumTag> tags = new ArrayList<>();

    private List<Document> images = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public BigDecimal getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(BigDecimal costPrice) {
        this.costPrice = costPrice;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public EnumColor getColor() {
        return color;
    }

    public void setColor(EnumColor color) {
        this.color = color;
    }

    public List<EnumTag> getTags() {
        return tags;
    }

    public void setTags(List<EnumTag> tags) {
        this.tags = tags;
    }

    public List<Document> getImages() {
        return images;
    }

    public void setImages(List<Document> images) {
        this.images = images;
    }
}
