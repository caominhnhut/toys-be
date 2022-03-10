package com.momo.toys.be.dto;

import com.paypal.api.payments.PayerInfo;
import com.paypal.api.payments.ShippingAddress;
import com.paypal.api.payments.Transaction;

public class PaymentDetailDTO{

    private PayerInfo payerInfo;

    private ShippingAddress shippingAddress;

    private Transaction transaction;

    public PaymentDetailDTO(PayerInfo payerInfo,Transaction transaction){
        this.payerInfo = payerInfo;
        this.transaction = transaction;
    }

    public PaymentDetailDTO(PayerInfo payerInfo,Transaction transaction, ShippingAddress shippingAddress){
        this.payerInfo = payerInfo;
        this.shippingAddress = shippingAddress;
        this.transaction = transaction;
    }

    public PayerInfo getPayerInfo(){
        return payerInfo;
    }

    public void setPayerInfo(PayerInfo payerInfo){
        this.payerInfo = payerInfo;
    }

    public ShippingAddress getShippingAddress(){
        return shippingAddress;
    }

    public void setShippingAddress(ShippingAddress shippingAddress){
        this.shippingAddress = shippingAddress;
    }

    public Transaction getTransaction(){
        return transaction;
    }

    public void setTransaction(Transaction transaction){
        this.transaction = transaction;
    }
}
