package com.momo.toys.be.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Embeddable
public class OrderDetailId implements Serializable {

    private OrderEntity orderEntity;
    private ProductEntity productEntity;

    @ManyToOne (cascade = CascadeType.ALL)
     public OrderEntity getOrderEntity() {
        return orderEntity;
    }

    public void setOrderEntity(OrderEntity orderEntity) {
        this.orderEntity = orderEntity;
    }

    @ManyToOne (cascade = CascadeType.ALL)
    public ProductEntity getProductEntity() {
        return productEntity;
    }

    public void setProductEntity(ProductEntity productEntity) {
        this.productEntity = productEntity;
    }
}
