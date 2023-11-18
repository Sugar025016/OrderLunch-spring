package com.order_lunch.service;

import java.util.List;

import com.order_lunch.entity.Address;
import com.order_lunch.entity.AddressData;

public interface IAddressService {
    List<Address> putUserAddress( int userId  ,List<Address> addresses); 
    Address address( AddressData addresses); 
    public void geocodeAddress(String address);
}

