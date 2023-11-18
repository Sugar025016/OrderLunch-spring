package com.order_lunch.model.response;

import com.order_lunch.entity.Shop;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShopNameItemResponse {

    private Integer id;

    private String name;

    public ShopNameItemResponse(Shop shop ) {
        this.id = shop.getId();
        this.name = shop.getName();
    }



    
}
