package com.order_lunch.model;

import org.springframework.beans.BeanUtils;

import com.order_lunch.entity.AddressData;

import lombok.Data;

@Data
public class AddressResponse {
    private int id;

    private int addressDataId;

    private String city;

    private String area;
    private String street;

    private String detail;

    private double lat;

    private double lng;

    public AddressResponse() {

    }

    public AddressResponse(com.order_lunch.entity.Address address) {
        AddressData addressData = address.getAddressData();
        BeanUtils.copyProperties(address, this);
        this.city = addressData.getCity();
        this.area = addressData.getArea();
        this.street = addressData.getStreet();
        this.addressDataId = addressData.getId();
    }

    public AddressResponse(com.order_lunch.entity.ShopAddress shopAddress) {
        AddressData addressData = shopAddress.getAddressData();
        BeanUtils.copyProperties(shopAddress, this);
        this.city = addressData.getCity();
        this.area = addressData.getArea();
        this.street = addressData.getStreet();
    }
}
