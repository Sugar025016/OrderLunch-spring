package com.order_lunch.service.Impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.order_lunch.entity.AddressData;
import com.order_lunch.model.request.AddressRequest;
import com.order_lunch.repository.IAddressDataRepository;
import com.order_lunch.service.IAddressDataService;

@Service
public class AddressDataService implements IAddressDataService {


    @Autowired
    IAddressDataRepository iAddressDataRepository;

    @Override
    public AddressData getAddressData(int addressId) {
        Optional<AddressData> byId = iAddressDataRepository.findById(addressId);
        AddressData orElseThrow = byId.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, AddressData.class.getName()));

        return orElseThrow;
    }

    @Override
    public AddressData getAddressData(AddressRequest addressRequest) {
        Optional<AddressData> byId = iAddressDataRepository.findByCityAndAreaAndStreet(addressRequest.getCity(),addressRequest.getArea(),addressRequest.getStreet());
        AddressData orElseThrow = byId.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, AddressData.class.getName()));

        return orElseThrow;
    }
}
