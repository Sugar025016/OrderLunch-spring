package com.order_lunch.service;

import java.util.List;

import com.order_lunch.entity.Cart;
import com.order_lunch.entity.User;
import com.order_lunch.model.request.CartRequest;
import com.order_lunch.model.response.ShopCartResponse;

public interface ICartService {

    List<Cart> getAllByUserId(int userId);

    int addCart(int userId,CartRequest cartRequest);
    
    boolean isCart(int userId);

    ShopCartResponse putCart(int userId, int cartId,CartRequest cartRequest);

    ShopCartResponse putCart(int userId, int cartId,int qty);

    ShopCartResponse deleteCart(int userId,int cartId);

    ShopCartResponse getCartByUserId(int id);
    
    User deleteAllCart( User user) ;

}
