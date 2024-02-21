package com.order_lunch.service;

import com.order_lunch.entity.AddressData;
import com.order_lunch.model.request.AddressRequest;

public interface IAddressDataService {
    AddressData getAddressData( int addressId);
    AddressData getAddressData( AddressRequest addressStreet);
}

