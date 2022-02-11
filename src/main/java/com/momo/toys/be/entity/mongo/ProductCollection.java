package com.momo.toys.be.entity.mongo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.format.annotation.DateTimeFormat;

import com.momo.toys.be.enumeration.EntityStatus;
import com.momo.toys.be.enumeration.EnumTag;
import com.momo.toys.be.factory.ConstantUtility;
import com.momo.toys.be.model.DocumentMeta;

@Document(collection = "product")
public class ProductCollection{

    @Id
    private String id;

    @Field(value = "code")
    private String code;

    @Indexed(unique = true)
    @Field(value = "name")
    private String name;

    @Field(value = "owner")
    private String owner;

    @Field(value = "description")
    private String description;

    @Field(value = "amount")
    private int amount;

    @Field(value = "cost_price")
    private BigDecimal costPrice;

    @Field(value = "price")
    private BigDecimal price;

    @Field(value = "tags")
    private List<EnumTag> tags = new ArrayList<>();

    @Field(value = "images")
    private List<DocumentMeta> images = new ArrayList<>();

    @Field(value = "status")
    private EntityStatus status;

    @DateTimeFormat(pattern = ConstantUtility.DATE_TIME_FORMAT)
    @Field(value = "created_date")
    private Date createdDate;

    @DateTimeFormat(pattern = ConstantUtility.DATE_TIME_FORMAT)
    @Field(value = "updated_date")
    private Date updatedDate;

    public String getId(){
        return id;
    }

    public void setId(String id){
        this.id = id;
    }

    public String getCode(){
        return code;
    }

    public void setCode(String code){
        this.code = code;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
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

    public String getOwner(){
        return owner;
    }

    public void setOwner(String owner){
        this.owner = owner;
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

    public List<EnumTag> getTags(){
        return tags;
    }

    public List<DocumentMeta> getImages(){
        return images;
    }

    public EntityStatus getStatus(){
        return status;
    }

    public void setStatus(EntityStatus status){
        this.status = status;
    }

    public Date getCreatedDate(){
        return createdDate;
    }

    public void setCreatedDate(Date createdDate){
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate(){
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate){
        this.updatedDate = updatedDate;
    }

    public void setTags(List<EnumTag> tags){
        this.tags = tags;
    }

    public void setImages(List<DocumentMeta> images){
        this.images = images;
    }
}
