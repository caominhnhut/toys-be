package com.momo.toys.be.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Set;

@Entity
@Table(name = "product")
public class ProductEntity extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "description")
    private String description;

    @Column(name = "amount")
    private int amount;

    @Column(name = "cost_price")
    private BigDecimal costPrice;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "main_image")
    private String mainImage;

    @OneToMany(mappedBy = "product")
    private Set<DocumentEntity> images;

    @OneToMany(mappedBy = "product")
    private Set<ReviewEntity> reviews;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private CategoryEntity categoryEntity;

    @OneToMany(mappedBy = "primaryKey.productEntity", cascade = CascadeType.ALL)
    private Set<OrderDetailEntity> orderDetailEntities;

    public CategoryEntity getCategoryEntity(){
        return categoryEntity;
    }

    public void setCategoryEntity(CategoryEntity categoryEntity){
        this.categoryEntity = categoryEntity;
    }

    @Column(name = "created_by")
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

    public Set<DocumentEntity> getImages(){
        return images;
    }

    public void setImages(Set<DocumentEntity> images){
        this.images = images;
    }

    public String getCreatedBy(){
        return createdBy;
    }

    public void setCreatedBy(String createdBy){
        this.createdBy = createdBy;
    }

    public Set<ReviewEntity> getReviews() {
        return reviews;
    }

    public void setReviews(Set<ReviewEntity> reviews) {
        this.reviews = reviews;
    }

    public Set<OrderDetailEntity> getOrderDetailEntities() {
        return orderDetailEntities;
    }

    public void setOrderDetailEntities(Set<OrderDetailEntity> orderDetailEntities) {
        this.orderDetailEntities = orderDetailEntities;
    }

    public ProductEntity(Long id, String name, String code, String description, int amount, BigDecimal costPrice, BigDecimal price, String mainImage, Set<DocumentEntity> images, Set<ReviewEntity> reviews, CategoryEntity categoryEntity, Set<OrderDetailEntity> orderDetailEntities, String createdBy) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.description = description;
        this.amount = amount;
        this.costPrice = costPrice;
        this.price = price;
        this.mainImage = mainImage;
        this.images = images;
        this.reviews = reviews;
        this.categoryEntity = categoryEntity;
        this.orderDetailEntities = orderDetailEntities;
        this.createdBy = createdBy;
    }

    public ProductEntity() {
    }

    public void addOrderDetail(OrderDetailEntity orderDetailEntity){
        this.orderDetailEntities.add(orderDetailEntity);
    }
}
