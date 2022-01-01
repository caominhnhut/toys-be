package com.momo.toys.be.model;

import java.math.BigDecimal;
import java.util.List;

public class Product{

    private Long id;

    private String name;

    private String code;

    private String description;

    private int amount;

    private BigDecimal costPrice;

    private BigDecimal price;

    private Long categoryId;

    private String mainImage;

    private List<Document> images;

    public Long getCategoryId(){
        return categoryId;
    }

    public void setCategoryId(Long categoryId){
        this.categoryId = categoryId;
    }

    private String createdBy;

    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getCode(){
        return code;
    }

    public void setCode(String code){
        this.code = code;
    }

    public String getDescription(){
        return description;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public int getAmount(){
        return amount;
    }

    public void setAmount(int amount){
        this.amount = amount;
    }

    public BigDecimal getCostPrice(){
        return costPrice;
    }

    public void setCostPrice(BigDecimal costPrice){
        this.costPrice = costPrice;
    }

    public BigDecimal getPrice(){
        return price;
    }

    public void setPrice(BigDecimal price){
        this.price = price;
    }

    public String getMainImage(){
        return mainImage;
    }

    public void setMainImage(String mainImage){
        this.mainImage = mainImage;
    }

    public List<Document> getImages(){
        return images;
    }

    public void setImages(List<Document> images){
        this.images = images;
    }

    public String getCreatedBy(){
        return createdBy;
    }

    public void setCreatedBy(String createdBy){
        this.createdBy = createdBy;
    }
}