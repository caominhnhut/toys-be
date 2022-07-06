package com.momo.toys.be.entity;

import com.fasterxml.jackson.annotation.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "orderz")
public class OrderEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @JsonIgnore
    @OneToMany(mappedBy = "primaryKey.orderEntity", cascade = CascadeType.ALL)
    private Set<OrderDetailEntity> orderDetailEntities = new HashSet<OrderDetailEntity>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity users) {
        this.user = users;
    }

    public Set<OrderDetailEntity> getOrderDetailEntities() {
        return orderDetailEntities;
    }

    public void setOrderDetailEntities(Set<OrderDetailEntity> orderDetailEntities) {
        this.orderDetailEntities = orderDetailEntities;
    }

    public void addOrderDetail(OrderDetailEntity orderDetailEntity) {
        this.orderDetailEntities.add(orderDetailEntity);
    }
}













