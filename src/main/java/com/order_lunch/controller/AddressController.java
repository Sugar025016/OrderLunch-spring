package com.order_lunch.controller;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.order_lunch.config.CustomUserDetails;
import com.order_lunch.entity.Address;
import com.order_lunch.entity.AddressData;
import com.order_lunch.entity.Cart;
import com.order_lunch.entity.User;
import com.order_lunch.model.AddressResponse;
import com.order_lunch.model.request.AddressRequest;
import com.order_lunch.repository.IAddressDataRepository;
import com.order_lunch.service.Impl.AddressDataService;
import com.order_lunch.service.Impl.AddressService;
import com.order_lunch.service.Impl.CartService;
import com.order_lunch.service.Impl.UserService;

@RestController
@RequestMapping("/address")
public class AddressController {

    @Autowired
    UserService userService;

    @Autowired
    CartService cartService;

    @Autowired
    IAddressDataRepository addressDataRepository;

    @Autowired
    AddressService addressService;

    @Autowired
    AddressDataService addressDataService;

    @RequestMapping(path = "", method = RequestMethod.GET)
    public ResponseEntity<List<AddressResponse>> getAddress(
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        User findByAccount = userService.findById(customUserDetails.getId());
        List<Address> prioritizeAddress = new ArrayList<Address>();
        if (findByAccount.getAddressDelivery() != null) {

            prioritizeAddress = addressService.prioritizeAddress(findByAccount.getAddresses(),
                    findByAccount.getAddressDelivery().getId());
        } else {
            prioritizeAddress = findByAccount.getAddresses();
        }
        List<AddressResponse> collect = prioritizeAddress.stream().map(v -> new AddressResponse(v))
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(collect);
    }

    @RequestMapping(path = "/google", method = RequestMethod.GET)
    public ResponseEntity<String> getGoogle(@AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody String address) {
        addressService.geocodeAddress(address);
        return ResponseEntity.ok().body("address2");
    }

    @RequestMapping(path = "", method = RequestMethod.POST)
    public ResponseEntity<AddressResponse> addAddress(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody AddressRequest addresses) {

        AddressData addressData = addressDataService.getAddressData(addresses);
        User user = userService.findById(customUserDetails.getId());

        Address address = addressService.addAddress(addressData, addresses.getDetail(), user);

        userService.addUserAddress(customUserDetails.getId(), address);

        return ResponseEntity.ok().body(new AddressResponse(address));
    }

    @Transactional
    @RequestMapping(path = "", method = RequestMethod.PUT)
    public ResponseEntity<Boolean> putAddress(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody AddressRequest addressRequest) {

        User user = userService.findById(customUserDetails.getId());
        if (user.getAddressDelivery().getId() == addressRequest.getId()) {
            List<Cart> carts = user.getCarts();
            if (carts.size() > 0) {
                cartService.deleteAllCart(user);
            }
        }
        Address putUserAddress = addressService.putUserAddress(customUserDetails.getId(), addressRequest);
        if (putUserAddress.getId() == null) {
            throw new ConcurrentModificationException("Encountered a serious system error");
        }

        return ResponseEntity.ok().body(true);
    }

    @Transactional
    @RequestMapping(path = "/{addressId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteAddress(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable int addressId) {

        User user = userService.findById(customUserDetails.getId());
        if (user.getAddressDelivery() != null && user.getAddressDelivery().getId() == addressId) {
            List<Cart> carts = user.getCarts();
            if (carts.size() > 0) {
                cartService.deleteAllCart(user);
            }
            userService.deleteAddressDelivery(customUserDetails.getId(), addressId);
        }

        addressService.deleteUserAddress(customUserDetails.getId(), addressId);

        return ResponseEntity.ok().build();

    }

}
