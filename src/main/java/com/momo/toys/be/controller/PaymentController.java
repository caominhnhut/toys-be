package com.momo.toys.be.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.momo.toys.be.dto.OrderDetailDTO;
import com.momo.toys.be.dto.PaymentDetailDTO;
import com.momo.toys.be.dto.Problem;
import com.momo.toys.be.factory.CommonUtility;
import com.momo.toys.be.factory.mapper.PaymentMapper;
import com.momo.toys.be.model.OrderDetail;
import com.momo.toys.be.service.PaymentService;
import com.paypal.api.payments.PayerInfo;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.PayPalRESTException;

@RestController
public class PaymentController{

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private CommonUtility commonUtility;

    @PostMapping("/payments/authorize")
    public ResponseEntity authorizePayment(@RequestBody OrderDetailDTO orderDetailDTO){
        OrderDetail orderDetail = PaymentMapper.mapToOrderDetail.apply(orderDetailDTO);

        try{
            String approvalLink = paymentService.authorizePayment(orderDetail);
            return ResponseEntity.ok(approvalLink);
        }catch(PayPalRESTException e){
            Problem problem = commonUtility.createInternalError(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problem);
        }
    }

    @GetMapping("/payments/review")
    public ResponseEntity reviewPayment(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId){
        Payment paymentDetail;
        try{
            paymentDetail = paymentService.getPaymentDetails(paymentId);

        }catch(PayPalRESTException e){
            Problem problem = commonUtility.createInternalError(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problem);
        }

        PayerInfo payerInfo = paymentDetail.getPayer().getPayerInfo();
        Transaction transaction = paymentDetail.getTransactions().get(0);

        return ResponseEntity.ok(ResponseEntity
                .ok(new PaymentDetailDTO(payerInfo, transaction, transaction.getItemList().getShippingAddress())));
    }

    @PostMapping("/payments/execute")
    public ResponseEntity executePayment(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId){
        try{
            Payment payment = paymentService.executePayment(paymentId, payerId);

            PayerInfo payerInfo = payment.getPayer().getPayerInfo();
            Transaction transaction = payment.getTransactions().get(0);
            return ResponseEntity.ok(new PaymentDetailDTO(payerInfo, transaction));

        }catch(PayPalRESTException e){
            Problem problem = commonUtility.createInternalError(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problem);
        }
    }
}
