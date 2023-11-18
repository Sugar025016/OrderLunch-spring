package com.order_lunch.service;

import com.order_lunch.model.request.CartRequest;
import com.order_lunch.model.response.ShopCartResponse;

public interface ICartService {

    ShopCartResponse getAllByUserId(int userId);

    int addCart(int userId,CartRequest cartRequest);


    ShopCartResponse putCart(int userId, int cartId,CartRequest cartRequest);

    ShopCartResponse putCart(int userId, int cartId,int qty);

    ShopCartResponse deleteCart(int userId,int cartId);
    
}
