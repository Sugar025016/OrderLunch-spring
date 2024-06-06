package com.order_lunch.controller;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import com.order_lunch.entity.AddressData;
import com.order_lunch.entity.Cart;
import com.order_lunch.entity.Shop;
import com.order_lunch.entity.User;
import com.order_lunch.enums.NewErrorStatus;
import com.order_lunch.model.AddressResponse;
import com.order_lunch.model.ErrorResponse;
import com.order_lunch.model.request.AddressRequest;
import com.order_lunch.model.request.PasswordRequest;
import com.order_lunch.model.request.UserPutRequest;
import com.order_lunch.model.request.UserRequest;
import com.order_lunch.model.response.ShopResponse;
import com.order_lunch.model.response.UserResponse;
import com.order_lunch.repository.IAddressDataRepository;
import com.order_lunch.service.Impl.AddressDataService;
import com.order_lunch.service.Impl.AddressService;
import com.order_lunch.service.Impl.CartService;
import com.order_lunch.service.Impl.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

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


    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<?> addUser(HttpSession session,
            @RequestBody() @Valid UserRequest userRequest) {
        String storedCaptcha = (String) session.getAttribute("captchaText");

        if (storedCaptcha == null) {

            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setCode(NewErrorStatus.CAPTCHA_ERROR.getKey());
            errorResponse.setMessage(NewErrorStatus.CAPTCHA_ERROR.getChinese());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);

        }

        if (!storedCaptcha.equals(userRequest.getVerifyCode())) {

            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setCode(NewErrorStatus.CAPTCHA_ERROR.getKey());
            errorResponse.setMessage(NewErrorStatus.CAPTCHA_ERROR.getChinese());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);

        }

        if (userService.accountExists(userRequest.getAccount())) {
            ErrorResponse errorResponse = new ErrorResponse(NewErrorStatus.ACCOUNT_EXISTS.getKey(),NewErrorStatus.ACCOUNT_EXISTS.getChinese());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        session.removeAttribute("captchaText"); // 驗證成功後從Session中移除
        userService.addMember(userRequest);
        

        return ResponseEntity.ok().build();
    }
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

    // @RequestMapping(path = "/address", method = RequestMethod.PUT)
    // public ResponseEntity<List<AddressResponse>> putAddress(
    // @AuthenticationPrincipal CustomUserDetails customUserDetails,
    // @RequestBody List<Address> addresses) {
    // List<Address> putUserAddress =
    // userService.putUserAddress(customUserDetails.getId(), addresses);
    // List<AddressResponse> collect = putUserAddress.stream().map(v -> new
    // AddressResponse(v))
    // .collect(Collectors.toList());
    // return ResponseEntity.ok().body(collect);
    // }

    // @RequestMapping(path = "/address", method = RequestMethod.PUT)
    // public ResponseEntity<List<AddressResponse>> putAddress(
    // @AuthenticationPrincipal CustomUserDetails customUserDetails,
    // @RequestBody List<AddressRequest> addresses) {

    // List<Address> addressList = addresses.stream().map(v -> new Address(v))
    // .collect(Collectors.toList());

    // List<Address> putUserAddress =
    // userService.putUserAddress(customUserDetails.getId(), addressList);
    // List<AddressResponse> collect = putUserAddress.stream().map(v -> new
    // AddressResponse(v))
    // .collect(Collectors.toList());
    // return ResponseEntity.ok().body(collect);
    // }

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

    // @Autowired
    // AddressService addressData;

    @RequestMapping(path = "/google", method = RequestMethod.GET)
    public ResponseEntity<String> getGoogle(@AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody String address) {
        addressService.geocodeAddress(address);
        return ResponseEntity.ok().body("address2");
    }

    @RequestMapping(path = "/address", method = RequestMethod.POST)
    public ResponseEntity<AddressResponse> addAddress(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody AddressRequest addresses) {

        AddressData addressData = addressDataService.getAddressData(addresses);
        User user=userService.findById(customUserDetails.getId());

        Address address = addressService.addAddress(addressData, addresses.getDetail(),user);

        userService.addUserAddress(customUserDetails.getId(), address);

        return ResponseEntity.ok().body(new AddressResponse(address));
    }

    @Transactional
    @RequestMapping(path = "/address", method = RequestMethod.PUT)
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
    @RequestMapping(path = "/address/{addressId}", method = RequestMethod.DELETE)
    public ResponseEntity<Boolean> deleteAddress(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable int addressId) {

        User user = userService.findById(customUserDetails.getId());
        boolean deleteAddressDelivery = false;
        if (user.getAddressDelivery().getId() == addressId) {
            List<Cart> carts = user.getCarts();
            if (carts.size() > 0) {
                cartService.deleteAllCart(user);
            }
            deleteAddressDelivery = userService.deleteAddressDelivery(customUserDetails.getId(), addressId);
        }

        boolean deleteUserAddress = addressService.deleteUserAddress(customUserDetails.getId(), addressId);
        if (deleteAddressDelivery && deleteUserAddress) {
            // throw new ConcurrentModificationException("Encountered a serious system
            // error");
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.badRequest().build();
    }

    @RequestMapping(path = "/address/delivery/{addressId}", method = RequestMethod.PUT)
    public ResponseEntity<?> putDeliveryAddress(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable int addressId) {

        // try {
        userService.putUserAddressDelivery(customUserDetails.getId(), addressId);
        // } catch (Exception ex) {
        // // return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body();
        // return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        // .body("Failed to save entity: " + ex.getMessage());
        // }
        return ResponseEntity.ok().build();

    }

}
