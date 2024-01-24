package com.order_lunch.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.order_lunch.entity.Address;
import com.order_lunch.entity.AddressData;
import com.order_lunch.repository.IAddressRepository;
import com.order_lunch.service.IAddressService;
import com.order_lunch.utils.GeocodingService;
import com.order_lunch.utils.GeocodingService.Coordinates;

@Service
public class AddressService implements IAddressService {

    @Autowired
    GeocodingService geocodingService;


    @Autowired
    IAddressRepository iAddressRepository;

    @Override
    public List<Address> putUserAddress(int userId, List<Address> addresses) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'putUserAddress'");
    }

    @Override
    public Address address(AddressData addresses) {

        return null;
    }

    @Override
    public void geocodeAddress(String address) {
        System.out.println(address);
        geocodingService.geocodeAddress(address);

    }

    @Override
    @Transactional
    public Address addAddress(AddressData addressData, String detail) {

        String addressStr = addressData.getCity() + addressData.getArea() + addressData.getStreet() + detail;

        Coordinates geocodeAddress = geocodingService.geocodeAddress(addressStr);

        Address address = new Address(addressData, detail, geocodeAddress.getLat(), geocodeAddress.getLng());

        Address save = iAddressRepository.save(address);

        return save;

    }

}
