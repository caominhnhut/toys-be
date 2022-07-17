package com.momo.toys.be.entity;

import java.math.BigDecimal;

import javax.persistence.AssociationOverride;
import javax.persistence.AssociationOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "order_item")
@AssociationOverrides({
        @AssociationOverride(name = "primaryKey.order",
                joinColumns = @JoinColumn(name = "order_id")),
        @AssociationOverride(name = "primaryKey.product",
                joinColumns = @JoinColumn(name = "product_id")) })
public class OrderItemEntity extends BaseEntity{

    @EmbeddedId
    private OrderItemId primaryKey = new OrderItemId();

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "unit_price", nullable = false)
    private BigDecimal unitPrice;

    @Column(name = "total_price", nullable = false)
    private BigDecimal totalPrice;

    public OrderItemEntity(OrderEntity orderEntity, ProductEntity productEntity){
        this.getPrimaryKey().setOrder(orderEntity);
        this.getPrimaryKey().setProduct(productEntity);
    }

    @Transient
    public OrderEntity getOrderEntity(){
        return primaryKey.getOrder();
    }

    public void setOrderEntity(OrderEntity orderEntity){
        this.primaryKey.setOrder(orderEntity);
    }

    @Transient
    public ProductEntity getProductEntity(){
        return primaryKey.getProduct();
    }

    public void setProductEntity(ProductEntity productEntity){
        this.primaryKey.setProduct(productEntity);
    }

    public OrderItemId getPrimaryKey(){
        return primaryKey;
    }

    public void setPrimaryKey(OrderItemId primaryKey){
        this.primaryKey = primaryKey;
    }

    public int getQuantity(){
        return quantity;
    }

    public void setQuantity(int quantity){
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice(){
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice){
        this.unitPrice = unitPrice;
    }

    public BigDecimal getTotalPrice(){
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice){
        this.totalPrice = totalPrice;
    }
}
