package com.momo.toys.be.entity.mongo;

import com.momo.toys.be.enumeration.EnumColor;
import com.momo.toys.be.enumeration.EnumSize;
import com.momo.toys.be.enumeration.EnumTag;
import com.momo.toys.be.model.FileData;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "product")
public class ProductCollection {

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

    @Field(value = "size")
    private EnumSize size;

    @Field(value = "color")
    private EnumColor color;

    @Field(value = "tags")
    private List<EnumTag> tags = new ArrayList<>();

    @Field(value = "images")
    private List<FileData> images = new ArrayList<>();

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

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public EnumSize getSize() {
        return size;
    }

    public void setSize(EnumSize size) {
        this.size = size;
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

    public List<FileData> getImages() {
        return images;
    }
}
