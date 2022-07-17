package com.momo.toys.be.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "orderz")
@Cache(
        usage = CacheConcurrencyStrategy.READ_WRITE
)
public class OrderEntity extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "customer_name", nullable = false)
    private String customerName;

    @Column(name = "employee_name")
    private String employeeName;

    @Column(name = "total_price", nullable = false)
    private BigDecimal totalPrice;

    @OneToMany(
            mappedBy = "primaryKey.order",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<OrderItemEntity> orderItems = new ArrayList<>();

    public void addProduct(ProductEntity product, int quantity){
        OrderItemEntity orderItem = new OrderItemEntity(this, product);
        orderItem.setQuantity(quantity);
        orderItem.setUnitPrice(product.getPrice());

        // TODO: will add the discount later
        BigDecimal totalPrice = orderItem.getUnitPrice().multiply(new BigDecimal(orderItem.getQuantity()));
        orderItem.setTotalPrice(totalPrice);

        orderItems.add(orderItem);
        product.getOrderItems().add(orderItem);
    }

    public void removeProduct(ProductEntity product){
        for(Iterator<OrderItemEntity> iterator = orderItems.iterator(); iterator.hasNext();){

            OrderItemEntity orderItem = iterator.next();

            if(orderItem.getPrimaryKey().getOrder().equals(this) && orderItem.getPrimaryKey().getProduct().equals(product)){
                iterator.remove();
                orderItem.getPrimaryKey().getProduct().getOrderItems().remove(orderItem);
                orderItem.getPrimaryKey().setOrder(null);
                orderItem.getPrimaryKey().setProduct(null);
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass())
            return false;

        OrderEntity order = (OrderEntity) o;
        return Objects.equals(id, order.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public String getCustomerName(){
        return customerName;
    }

    public void setCustomerName(String customerName){
        this.customerName = customerName;
    }

    public String getEmployeeName(){
        return employeeName;
    }

    public void setEmployeeName(String employeeName){
        this.employeeName = employeeName;
    }

    public BigDecimal getTotalPrice(){
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice){
        this.totalPrice = totalPrice;
    }

    public List<OrderItemEntity> getOrderItems(){
        return orderItems;
    }

    public void setOrderItems(List<OrderItemEntity> orderItems){
        this.orderItems = orderItems;
    }
}
