package com.order_lunch.service.Impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.order_lunch.entity.Address;
import com.order_lunch.entity.AddressData;
import com.order_lunch.model.request.AddressRequest;
import com.order_lunch.repository.IAddressRepository;
import com.order_lunch.service.IAddressService;
import com.order_lunch.utils.GeocodingService;
import com.order_lunch.utils.GeocodingService.Coordinates;

@Service
public class AddressService implements IAddressService {

    @Autowired
    GeocodingService geocodingService;

    @Autowired
    AddressDataService addressDataService;

    @Autowired
    IAddressRepository iAddressRepository;

    // @Autowired
    // UserService userService;

    @Override
    @Transactional
    public Address putUserAddress(int userId, AddressRequest addressRequest) {

        Address findAddress = getUserAddress(userId,addressRequest.getId());

        AddressData addressData = addressDataService.getAddressData(addressRequest);

        findAddress.setAddressData(addressData);
        findAddress.setDetail(addressRequest.getDetail());

        String addressStr = addressData.getCity() + addressData.getArea() + addressData.getStreet()
                + findAddress.getDetail();
        Coordinates geocodeAddress = geocodingService.geocodeAddress(addressStr);

        findAddress.setLat(geocodeAddress.getLat());
        findAddress.setLng(geocodeAddress.getLng());

        Address save = iAddressRepository.save(findAddress);

        return save;

    }

    @Override
    @Transactional
    public boolean deleteUserAddress(int userId, int addressId) {

        Address findAddress = getUserAddress(userId,addressId);

        // 不佳判斷式，會出現 >> 
        // Null type mismatch: required '@NonNull Address' but the provided value is inferred as @Nullable
        if (findAddress != null) {
            iAddressRepository.delete(findAddress);
        }
        return !iAddressRepository.existsById(addressId);

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

    @Override
    public List<Address> addAddresses(List<AddressRequest> addresses) {
        List<Address> arrayList = new ArrayList<Address>();
        addresses.forEach(v -> {
            AddressData addressData = addressDataService.getAddressData(v.getId());

            arrayList.add(this.addAddress(addressData, v.getDetail()));
        });

        return arrayList;

    }

    @Override
    public Address getUserAddress(int userId, int addressId) {
        Optional<Address> byUserIdAndAddressId =
        iAddressRepository.findByUserIdAndId(userId, addressId);

        Address findAddress = byUserIdAndAddressId.orElseThrow(() -> new
        ResponseStatusException(HttpStatus.NOT_FOUND, "Address"));
        return findAddress;
    }

    @Override
    public boolean isUserAddress(int userId, int addressId) {
 
        iAddressRepository.existsByIdAndUser_id(addressId,userId);

        return iAddressRepository.existsByIdAndUser_id(addressId,userId);
    }




    @Override
    public List<Address> prioritizeAddress(List<Address> addressList, int idToPrioritize) {
        Address addressToPrioritize = null;

        // 查找ID为8的地址并移除
        Iterator<Address> iterator = addressList.iterator();
        while (iterator.hasNext()) {
            Address address = iterator.next();
            if (address.getId() == idToPrioritize) {
                addressToPrioritize = address;
                iterator.remove();
                break;
            }
        }

        // 将ID为8的地址插入到列表的第一个位置
        if (addressToPrioritize != null) {
            addressList.add(0, addressToPrioritize);
        }

        return addressList;
    }

}
