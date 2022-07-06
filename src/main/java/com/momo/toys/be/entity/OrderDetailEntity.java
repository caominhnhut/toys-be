package com.momo.toys.be.entity;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
@Table(name = "order_detail")
@AssociationOverrides({
        @AssociationOverride(name = "primaryKey.orderEntity",
                joinColumns = @JoinColumn(name = "order_id")),
        @AssociationOverride(name = "primaryKey.productEntity",
                joinColumns = @JoinColumn(name = "product_id"))})

public class OrderDetailEntity extends BaseEntity {
    private OrderDetailId primaryKey = new OrderDetailId();

    @EmbeddedId
    public OrderDetailId getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(OrderDetailId primaryKey) {
        this.primaryKey = primaryKey;
    }

    public void setOrderEntity(OrderEntity orderEntity) {
        this.getPrimaryKey().setOrderEntity(orderEntity);
    }

    @Transient
    public OrderEntity getOrderEntity(OrderEntity orderEntity){
        return this.getPrimaryKey().getOrderEntity();
    }


    public void setProductEntity(ProductEntity productEntity){
        this.getPrimaryKey().setProductEntity(productEntity);
    }

    @Transient
    public ProductEntity getProductEntity(){
        return this.getPrimaryKey().getProductEntity();
    }

    @NotNull
    @Min(value = 0)
    @Column(name = "quantity", nullable = false)
    private int quantity;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "total_price", precision = 21, scale = 2, nullable = false)
    private BigDecimal totalPrice;

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

}
