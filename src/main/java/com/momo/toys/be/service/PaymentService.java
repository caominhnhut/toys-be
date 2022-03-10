package com.momo.toys.be.service;

import com.momo.toys.be.model.OrderDetail;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;

public interface PaymentService{

    String authorizePayment(OrderDetail orderDetail) throws PayPalRESTException;

    Payment getPaymentDetails(String paymentId) throws PayPalRESTException;

    Payment executePayment(String paymentId, String payerId) throws PayPalRESTException;
}
