package com.order_lunch.service;

import java.util.List;

import com.order_lunch.entity.Address;
import com.order_lunch.entity.AddressData;
import com.order_lunch.model.request.AddressRequest;

public interface IAddressService {
    boolean deleteUserAddress( int userId  , int addressId); 
    Address putUserAddress( int userId  ,AddressRequest addressRequest); 
    public void geocodeAddress(String address);
    Address addAddress( AddressData addresses , String  detail); 

    List<Address> addAddresses( List<AddressRequest> addresses ); 
    Address getUserAddress( int userId  , int addressId); 
    boolean isUserAddress( int userId  , int addressId); 
    List<Address> prioritizeAddress(List<Address> addressList, int idToPrioritize);
}

