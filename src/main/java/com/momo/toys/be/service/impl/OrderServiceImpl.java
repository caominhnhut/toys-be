package com.momo.toys.be.service.impl;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.momo.toys.be.entity.OrderEntity;
import com.momo.toys.be.entity.OrderItemEntity;
import com.momo.toys.be.entity.ProductEntity;
import com.momo.toys.be.enumeration.EntityStatus;
import com.momo.toys.be.model.CartItem;
import com.momo.toys.be.model.ShoppingCart;
import com.momo.toys.be.repository.OrderRepository;
import com.momo.toys.be.repository.ProductRepository;
import com.momo.toys.be.service.AccountService;
import com.momo.toys.be.service.OrderService;

import javassist.NotFoundException;

@Service
public class OrderServiceImpl implements OrderService{

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private AccountService accountService;

    @Override
    public Long create(ShoppingCart shoppingCart) throws NotFoundException{

        OrderEntity orderEntity = buildOrderEntity(shoppingCart);

        Authentication authentication = accountService.getAuthorizedAccount();
        orderEntity.setCustomerName(authentication.getName());

        try{
            orderRepository.save(orderEntity);
        }catch(Exception e){
            e.printStackTrace();
        }

        return orderEntity.getId();
    }

    private OrderEntity buildOrderEntity(ShoppingCart shoppingCart) throws NotFoundException{

        List<Long> productIds = shoppingCart.getCartItems().stream().map(CartItem::getProductId).collect(Collectors.toList());

        Collection<ProductEntity> productEntities = productRepository.findByIDs(productIds, EntityStatus.ACTIVATED);

        OrderEntity orderEntity = new OrderEntity();

        for(CartItem cartItem: shoppingCart.getCartItems()){
            // find product to get the corresponding quantity
            Optional<ProductEntity> optProduct = productEntities
                    .stream()
                    .filter(p-> Objects.equals(p.getId(), cartItem.getProductId())).findFirst();

            if(!optProduct.isPresent()){
                throw new NotFoundException(String.format("The product with id [%s] not found", cartItem.getProductId()));
            }

            orderEntity.addProduct(optProduct.get(), cartItem.getQuantity());
        }

        // calculate the total price of order by items in cart
        BigDecimal totalPrice = orderEntity.getOrderItems().stream().map(OrderItemEntity::getTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
        orderEntity.setTotalPrice(totalPrice);

        return orderEntity;
    }
}
