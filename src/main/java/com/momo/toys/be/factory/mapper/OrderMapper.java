package com.momo.toys.be.factory.mapper;

import com.momo.toys.be.dto.OrderDetailDto;
import com.momo.toys.be.dto.OrderDto;
import com.momo.toys.be.entity.OrderDetailEntity;
import com.momo.toys.be.entity.OrderEntity;
import com.momo.toys.be.entity.UserEntity;
import com.momo.toys.be.model.Order;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class OrderMapper {

    public static final Function<List<com.momo.toys.be.order.Order>, Set<OrderDetailEntity>> mapDtoToOderDetail = orders -> orders.stream().map(order -> {
        OrderDetailEntity orderDetailEntity = new OrderDetailEntity();
        orderDetailEntity.setQuantity(order.getQuantity());
        return orderDetailEntity;
    }).collect(Collectors.toSet());

    public static final Function<List<com.momo.toys.be.order.Order>, List<Order>> mapDtotoModel = orders -> orders.stream().map(order -> {
        Order orderModel = new Order();
        orderModel.setQuantity(order.getQuantity());
        orderModel.setId(order.getId());
        return orderModel;
    }).collect(Collectors.toList());


    public static final Function<List<OrderEntity>, List<OrderDto>> mapEntitytoOrderDto = orders -> orders.stream().map(order -> {
        OrderDto orderDto = new OrderDto();
        orderDto.setOrderId(order.getId());
        orderDto.setCreatedDate(order.getCreatedDate());

        UserEntity userEntity = order.getUser();
        orderDto.setEmail(userEntity.getEmail());

        Set<OrderDetailEntity> orderDetailEntities = order.getOrderDetailEntities();
        List<OrderDetailDto> orderDetailDtos = new ArrayList<>();
        BigDecimal sum = BigDecimal.valueOf(0);

        for (OrderDetailEntity orderDetailEntity : orderDetailEntities) {
            OrderDetailDto detailDto = new OrderDetailDto();
            detailDto.setProductName(orderDetailEntity.getProductEntity().getName());
            detailDto.setQuantity(orderDetailEntity.getQuantity());
            detailDto.setTotalPrice(orderDetailEntity.getTotalPrice());
            orderDetailDtos.add(detailDto);
            sum = sum.add(orderDetailEntity.getTotalPrice());
        }

        orderDto.setTotalSold(sum);
        orderDto.setOrderDetail(orderDetailDtos);

        return orderDto;
    }).collect(Collectors.toList());

}
