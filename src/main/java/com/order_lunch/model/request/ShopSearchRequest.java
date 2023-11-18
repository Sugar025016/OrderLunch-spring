package com.order_lunch.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class ShopSearchRequest {

    String city;

    String area;

    Integer categoryId;

    String other;

    public ShopSearchRequest(String city, String area, Integer categoryId, String other) {
        this.city = city;
        this.area = area;
        this.categoryId = categoryId;
        this.other = other;
    }

}
