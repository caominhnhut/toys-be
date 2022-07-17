package com.momo.toys.be.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

@Embeddable
public class OrderItemId implements Serializable{

    @ManyToOne(cascade = CascadeType.ALL)
    private OrderEntity order;

    @ManyToOne(cascade = CascadeType.ALL)
    private ProductEntity product;

    public OrderEntity getOrder(){
        return order;
    }

    public void setOrder(OrderEntity order){
        this.order = order;
    }

    public ProductEntity getProduct(){
        return product;
    }

    public void setProduct(ProductEntity product){
        this.product = product;
    }
}
