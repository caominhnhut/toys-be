package com.momo.toys.be.dto;

public class OrderDetailDTO{

    private String productName;

    private float subtotal;

    private float shipping;

    private float tax;

    private float total;

    public String getProductName(){
        return productName;
    }

    public void setProductName(String productName){
        this.productName = productName;
    }

    public float getSubtotal(){
        return subtotal;
    }

    public void setSubtotal(float subtotal){
        this.subtotal = subtotal;
    }

    public float getShipping(){
        return shipping;
    }

    public void setShipping(float shipping){
        this.shipping = shipping;
    }

    public float getTax(){
        return tax;
    }

    public void setTax(float tax){
        this.tax = tax;
    }

    public float getTotal(){
        return total;
    }

    public void setTotal(float total){
        this.total = total;
    }
}
