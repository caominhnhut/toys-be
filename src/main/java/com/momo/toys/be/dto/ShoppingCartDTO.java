package com.momo.toys.be.dto;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCartDTO{

    private List<CartItemDTO> cartItems;

    public List<CartItemDTO> getCartItems(){

        if(cartItems == null){
            cartItems = new ArrayList<>();
        }

        return cartItems;
    }
}
