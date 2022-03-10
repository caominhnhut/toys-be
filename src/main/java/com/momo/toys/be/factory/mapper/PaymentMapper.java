package com.momo.toys.be.factory.mapper;

import java.util.function.Function;

import com.momo.toys.be.dto.OrderDetailDTO;
import com.momo.toys.be.model.OrderDetail;

public class PaymentMapper{

    public static Function<OrderDetailDTO, OrderDetail> mapToOrderDetail = orderDetailDTO -> new OrderDetail(orderDetailDTO.getProductName(), orderDetailDTO.getSubtotal(), orderDetailDTO.getShipping(), orderDetailDTO.getTax(), orderDetailDTO.getTotal());
}
