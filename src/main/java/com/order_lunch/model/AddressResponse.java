package com.order_lunch.model;

import org.springframework.beans.BeanUtils;

import com.order_lunch.entity.AddressData;

import lombok.Data;

@Data
public class AddressResponse {
    private Integer id;

    private String city;

    private String area;

    private String detail;

    public AddressResponse() {

    }

    public AddressResponse(com.order_lunch.entity.Address address) {
        AddressData addressData = address.getAddressData();
        BeanUtils.copyProperties(address, this);
        this.city = addressData.getCity();
        this.area = addressData.getArea();
    }
}
