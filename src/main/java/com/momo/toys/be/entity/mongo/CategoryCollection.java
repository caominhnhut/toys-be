package com.momo.toys.be.entity.mongo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.format.annotation.DateTimeFormat;

import com.momo.toys.be.enumeration.EntityStatus;
import com.momo.toys.be.factory.ConstantUtility;

@Document(value = "category")
public class CategoryCollection{

    @Id
    private String id;

    @Field(value = "status")
    private EntityStatus status;

    @DateTimeFormat(pattern = ConstantUtility.DATE_TIME_FORMAT)
    @Field(value = "created_date")
    private Date createdDate;

    @DateTimeFormat(pattern = ConstantUtility.DATE_TIME_FORMAT)
    @Field(value = "updated_date")
    private Date updatedDate;

    @Field(value = "name")
    private String name;

    @DBRef
    private List<ProductCollection> products = new ArrayList<>();

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public List<ProductCollection> getProducts(){
        return products;
    }

    public String getId(){
        return id;
    }

    public void setId(String id){
        this.id = id;
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
}
