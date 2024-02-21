package com.order_lunch.service;

import com.order_lunch.entity.AddressData;
import com.order_lunch.entity.ShopAddress;
import com.order_lunch.model.request.AddressRequest;

public interface IShopAddressService {
    public void geocodeAddress(String address);

    ShopAddress addAddress(AddressData addresses, String detail);

    ShopAddress putShopAddress(ShopAddress shopAddress, AddressRequest address);

}
