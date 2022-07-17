package com.momo.toys.be.model;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCart{

    private List<CartItem> cartItems;

    public List<CartItem> getCartItems(){

        if(cartItems == null){
            cartItems = new ArrayList<>();
        }

        return cartItems;
    }
}
