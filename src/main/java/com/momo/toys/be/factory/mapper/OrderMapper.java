package com.momo.toys.be.factory.mapper;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.momo.toys.be.dto.ShoppingCartDTO;
import com.momo.toys.be.model.CartItem;
import com.momo.toys.be.model.ShoppingCart;

public class OrderMapper{

    private OrderMapper(){
        // hide constructor
    }

    public static Function<ShoppingCartDTO, ShoppingCart> mapFromDTO = dto->{

        List<CartItem> cartItems = dto.getCartItems().stream().map(c -> {
            CartItem cartItem = new CartItem();
            cartItem.setProductId(c.getProductId());
            cartItem.setQuantity(c.getQuantity());
            return cartItem;
        }).collect(Collectors.toList());

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.getCartItems().addAll(cartItems);

        return shoppingCart;
    };

}
