package com.order_lunch.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.order_lunch.config.CustomUserDetails;
import com.order_lunch.entity.Address;
import com.order_lunch.entity.Shop;
import com.order_lunch.entity.User;
import com.order_lunch.model.AddressResponse;
import com.order_lunch.model.request.PasswordRequest;
import com.order_lunch.model.request.UserPutRequest;
import com.order_lunch.model.response.ShopResponse;
import com.order_lunch.model.response.UserResponse;
import com.order_lunch.repository.IAddressDataRepository;
import com.order_lunch.service.Impl.AddressService;
import com.order_lunch.service.Impl.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    IAddressDataRepository addressDataRepository;

    // CustomUserDetails
    @RequestMapping(path = "", method = RequestMethod.GET)
    public ResponseEntity<UserResponse> getUser(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        User findByAccount = userService.findById(customUserDetails.getId());
        UserResponse userResponse = new UserResponse(findByAccount);
        return ResponseEntity.ok().body(userResponse);
    }

    @RequestMapping(path = "", method = RequestMethod.PUT)
    public ResponseEntity<Boolean> putUser(@RequestBody UserPutRequest userPutRequest,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        boolean putUser = userService.putUser(userPutRequest, customUserDetails.getId());
        return ResponseEntity.ok().body(putUser);
    }

    @RequestMapping(path = "/pwd", method = RequestMethod.PUT)
    public ResponseEntity<Boolean> putUserPassword(@RequestBody PasswordRequest passwordRequest,
            @RequestBody UserPutRequest userPutRequest, HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        boolean putUser = userService.putUserPassword(passwordRequest, customUserDetails.getId());

        SecurityContextHolder.clearContext();
        Cookie cookie = new Cookie(authentication.getName(), null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);

        return ResponseEntity.ok().body(putUser);
    }

    @RequestMapping(path = "/favorite", method = RequestMethod.GET)
    public ResponseEntity<List<ShopResponse>> getLoves(
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        List<Shop> findLoveByAccount = userService.findLoveByAccount(customUserDetails.getId());
        List<ShopResponse> collect = findLoveByAccount.stream().map(v -> new ShopResponse(v))
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(collect);
    }

    @RequestMapping(path = "/favorite/{shopId}", method = RequestMethod.PUT)
    public ResponseEntity<Boolean> addOrDeleteUserLove(@PathVariable int shopId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Boolean findLoveByAccount = userService.addOrDeleteUserLove(customUserDetails.getId(),
                shopId);
        return ResponseEntity.ok().body(findLoveByAccount);
    }

    @RequestMapping(path = "/address", method = RequestMethod.GET)
    public ResponseEntity<List<AddressResponse>> getAddress(
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        User findByAccount = userService.findById(customUserDetails.getId());
        List<AddressResponse> collect = findByAccount.getAddresses().stream().map(v -> new AddressResponse(v))
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(collect);
    }

    @RequestMapping(path = "/address", method = RequestMethod.PUT)
    public ResponseEntity<List<AddressResponse>> putAddress(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody List<Address> addresses) {
        List<Address> putUserAddress = userService.putUserAddress(customUserDetails.getId(), addresses);
        List<AddressResponse> collect = putUserAddress.stream().map(v -> new AddressResponse(v))
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(collect);
    }

    // 新增database 的 addressData
    // @RequestMapping(path = "/address", method = RequestMethod.POST)
    // public ResponseEntity<List<AddressData>> Address(@AuthenticationPrincipal
    // CustomUserDetails customUserDetails,
    // @RequestBody List<AddressDataRequest> addressDataRequests) {
    // ArrayList<AddressData> arrayList = new ArrayList<AddressData>();
    // addressDataRequests.stream().forEach(c -> c.getArea().stream().forEach(a -> {
    // List<AddressData> arrayList2 = a.getStreets().stream()
    // .map(s -> new AddressData(s.getStreetKey(), c.getCityName(), a.getAreaName(),
    // s.getStreetName()))
    // .collect(Collectors.toList());
    // arrayList.addAll(arrayList2);
    // }));
    // List<AddressData> saveAll = addressDataRepository.saveAll(arrayList);
    // return ResponseEntity.ok().body(saveAll);
    // }

    @Autowired
    AddressService addressData;

    @RequestMapping(path = "/google", method = RequestMethod.GET)
    public ResponseEntity<String> getGoogle(@AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody String address) {
        addressData.geocodeAddress(address);
        return ResponseEntity.ok().body("address2");
    }
}
