package com.momo.toys.be.service.impl;

import com.momo.toys.be.account.Problem;
import com.momo.toys.be.dto.OrderDto;
import com.momo.toys.be.entity.OrderDetailEntity;
import com.momo.toys.be.entity.OrderEntity;
import com.momo.toys.be.entity.ProductEntity;
import com.momo.toys.be.entity.UserEntity;
import com.momo.toys.be.enumeration.EntityStatus;
import com.momo.toys.be.enumeration.SupportedType;
import com.momo.toys.be.exception.ValidationException;
import com.momo.toys.be.factory.CommonUtility;
import com.momo.toys.be.factory.mapper.OrderMapper;
import com.momo.toys.be.model.Order;
import com.momo.toys.be.repository.OrderDetailRepository;
import com.momo.toys.be.repository.OrderRepository;
import com.momo.toys.be.repository.ProductRepository;
import com.momo.toys.be.repository.UserRepository;
import com.momo.toys.be.service.AccountService;
import com.momo.toys.be.service.OrderService;
import com.momo.toys.be.validation.ValidationData;
import com.momo.toys.be.validation.ValidationProvider;
import javassist.NotFoundException;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;

import static java.math.BigDecimal.valueOf;
import static org.springframework.http.ResponseEntity.status;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ValidationProvider validationProvider;

    @Autowired
    private CommonUtility commonUtility;

    private Function<Order, Problem> validatorOrder = order -> {

        Optional<ProductEntity> existingProductEntityOptional = productRepository.findById(order.getId());
        ValidationData validationData = new ValidationData();
        validationData.setAmountProduct(0);
        existingProductEntityOptional.ifPresent(productEntity -> validationData.setAmountProduct(productEntity.getAmount()));

        validationData.setQuantityProduct(order.getQuantity());
        try {
            validationProvider.executeValidators(validationData, SupportedType.ORDER_VERIFY_AMOUNT);
        } catch (ValidationException e) {
            return commonUtility.createProblem(HttpStatus.INTERNAL_SERVER_ERROR.toString(), HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }

        return new Problem();
    };


    @Override
    public ResponseEntity create(List<Order> orders) throws NotFoundException, InterruptedException, ValidationException {

        OrderEntity orderEntity = new OrderEntity();
        String name = accountService.getAuthorizedAccount().getName();
        UserEntity userEntity = accountService.findUserByName(name);
        orderEntity.setUser(userEntity);
        orderRepository.save(orderEntity);
        for (Order order : orders) {

            Optional<ProductEntity> existingProductEntityOptional = productRepository.findById(order.getId());

            Problem problem = validatorOrder.apply(order);
            if (Strings.isNotEmpty(problem.getTitle())) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problem);
            }
            ProductEntity productEntity = existingProductEntityOptional.get();
            OrderDetailEntity orderDetailEntity = new OrderDetailEntity();
            orderDetailEntity.setQuantity(order.getQuantity());
            orderDetailEntity.setProductEntity(productEntity);
            orderDetailEntity.setOrderEntity(orderEntity);
            BigDecimal totalPrice = productEntity.getPrice().multiply(valueOf(order.getQuantity()));
            orderDetailEntity.setTotalPrice(totalPrice);

            int productAmount = productEntity.getAmount() - order.getQuantity();
            productEntity.setAmount(productAmount);

            orderDetailRepository.save(orderDetailEntity);

        }

        return status(HttpStatus.CREATED).body(orderEntity.getId());
    }


    @Override
    public Long update(List<Order> orders, final Long orderId) throws NotFoundException, InterruptedException, ValidationException {

        Optional<OrderEntity> optionalOrderEntity = orderRepository.findById(orderId);

        String name = accountService.getAuthorizedAccount().getName();

        if (optionalOrderEntity.isPresent() && name.equals(optionalOrderEntity.get().getUser().getEmail())) {

            for (Order order : orders) {

                Optional<ProductEntity> existingProductEntityOptional = productRepository.findById(order.getId());

                if (!existingProductEntityOptional.isPresent()) {
                    throw new ValidationException(String.format("Product with id [%s] not found", order.getId()));
                }

                if (existingProductEntityOptional.get().getAmount() < order.getQuantity()) {

                    throw new ValidationException(String.format("The amount of product with id [%s] is over current amount", order.getId()));

                }

                OrderDetailEntity orderDetailEntity = new OrderDetailEntity();
                orderDetailEntity.setOrderEntity(optionalOrderEntity.get());
                orderDetailEntity.setQuantity(order.getQuantity());
                orderDetailEntity.setProductEntity(existingProductEntityOptional.get());
                BigDecimal totalPrice = existingProductEntityOptional.get().getPrice().multiply(valueOf(order.getQuantity()));

                orderDetailEntity.setTotalPrice(totalPrice);

                orderDetailRepository.save(orderDetailEntity);

            }
        }
        return optionalOrderEntity.get().getId();
    }


    @Override
    public Boolean removeProductOrderForUser(final Long orderId) throws NotFoundException, InterruptedException {

        Optional<OrderEntity> optionalOrderEntity = orderRepository.findById(orderId);

        if (!optionalOrderEntity.isPresent()) {
            throw new NotFoundException(String.format("Order with id [%s] not found", orderId));
        }

        OrderEntity orderEntity = optionalOrderEntity.get();
        orderEntity.setStatus(EntityStatus.DELETED);

        orderRepository.delete(orderEntity);
        return true;
    }


    @Override
    public List<ProductEntity> getAllProductsByOrder(Long orderId) throws NotFoundException {

        Optional<OrderEntity> optionalOrderEntity = orderRepository.findById(orderId);

        if (!optionalOrderEntity.isPresent()) {
            throw new NotFoundException(String.format("Order with id [%s] not found", orderId));
        }

        OrderDetailEntity orderDetailEntity = new OrderDetailEntity();
        orderDetailEntity.getOrderEntity(optionalOrderEntity.get());

        ProductEntity productEntity = orderDetailEntity.getProductEntity();
        List<ProductEntity> productEntites = Arrays.asList(productEntity);

        return productEntites;
    }

    @Override
    public List<OrderDto> getOrderByUser(Long userId) throws ValidationException {

        List<OrderEntity> orderEntities = orderRepository.findOrderByUser(userId);

        if (orderEntities.isEmpty()) {
            throw new ValidationException(String.format("User with id [%s] not found", userId));
        }

        return OrderMapper.mapEntitytoOrderDto.apply(orderEntities);
    }

    @Override
    public List<OrderDto> getOrderbyDate(Date fromDate, Date toDate) {

        Calendar calFromDate = Calendar.getInstance();
        calFromDate.setTime(fromDate);
        calFromDate.set(Calendar.HOUR, 0);
        calFromDate.set(Calendar.MINUTE, 0);
        calFromDate.set(Calendar.SECOND, 0);
        Date fromDateTime = calFromDate.getTime();

        Calendar calToDate = Calendar.getInstance();
        calToDate.setTime(toDate);
        calToDate.add(Calendar.DAY_OF_MONTH, 1);
        calToDate.set(Calendar.HOUR, 0);
        calToDate.set(Calendar.MINUTE, 0);
        calToDate.set(Calendar.SECOND, 0);
        Date toDateTime = calToDate.getTime();

        List<OrderEntity> orderEntities = orderRepository.findOrderByDate(fromDateTime, toDateTime);

        return OrderMapper.mapEntitytoOrderDto.apply(orderEntities);

    }
}


