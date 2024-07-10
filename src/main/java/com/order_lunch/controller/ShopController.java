package com.order_lunch.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.order_lunch.config.CustomUserDetails;
import com.order_lunch.entity.Shop;
import com.order_lunch.entity.User;
import com.order_lunch.enums.NewErrorStatus;
import com.order_lunch.model.ErrorResponse;
import com.order_lunch.model.request.BackstageShopPutRequest;
import com.order_lunch.model.request.ShopRequest;
import com.order_lunch.model.request.ShopSearchRequest;
import com.order_lunch.model.response.SellShopResponse;
import com.order_lunch.model.response.ShopDetailResponse;
import com.order_lunch.model.response.ShopNameItemResponse;
import com.order_lunch.model.response.ShopResponse;
import com.order_lunch.service.Impl.ShopService;
import com.order_lunch.service.Impl.UserService;

@RestController
@RequestMapping("/shop")
public class ShopController {

    @Autowired
    ShopService shopService;

    @Autowired
    UserService userService;

    @RequestMapping(path = "", method = RequestMethod.GET)
    public ResponseEntity<Page<ShopResponse>> getShops(@AuthenticationPrincipal CustomUserDetails customUserDetails,
            ShopSearchRequest shopRequest,
            @PageableDefault(page = 0, size = 5) Pageable pageable) {

        Page<ShopResponse> findShops = null;

        if (customUserDetails != null && customUserDetails.getId() != 0) {
            User user = userService.findById(customUserDetails.getId());

            findShops = shopService.findShops(user, shopRequest, pageable);
        } else {
            findShops = shopService.findShops(shopRequest, pageable);
        }

        findShops.stream().forEach(v -> {
            v.setImgUrl(v.getImgUrl());
        });
        return ResponseEntity.ok().body(findShops);
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<ShopDetailResponse> getShop(@PathVariable() int id) {

        ShopDetailResponse shopResponse = new ShopDetailResponse(shopService.getShopById(id));
        return ResponseEntity.ok().body(shopResponse);
    }

    @RequestMapping(path = "/sell", method = RequestMethod.GET)
    public ResponseEntity<List<ShopNameItemResponse>> getShopsName(
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        List<Shop> findShops = shopService.getShopsByUserId(customUserDetails.getId());
        List<ShopNameItemResponse> collect = findShops.stream().map(v -> new ShopNameItemResponse(v))
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(collect);
    }

    @RequestMapping(path = "/sell/{id}", method = RequestMethod.GET)
    public ResponseEntity<SellShopResponse> getSellShop(@PathVariable() int id,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        SellShopResponse sellShopResponse = new SellShopResponse(shopService.getShop(customUserDetails.getId(), id));
        return ResponseEntity.ok().body(sellShopResponse);
    }

    @RequestMapping(path = "/sell", method = RequestMethod.PUT)
    public ResponseEntity<String> putShop(@RequestBody @Valid BackstageShopPutRequest shopPutRequest,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        shopService.putShop(shopPutRequest);
        return ResponseEntity.ok().build();
        // return ResponseEntity.ok().body(shopService.putShop(shopPutRequest));
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<?> addShop(HttpSession session,
            @RequestBody() @Valid ShopRequest shopRequest,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        ErrorResponse errorResponse = new ErrorResponse();

        String storedCaptcha = (String) session.getAttribute("captchaText");

        if (storedCaptcha == null || !storedCaptcha.equals(shopRequest.getCaptcha())) {

            errorResponse.setCode(NewErrorStatus.CAPTCHA_MISTAKE.getKey());
            errorResponse.setMessage(NewErrorStatus.CAPTCHA_MISTAKE.getChinese());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
        session.removeAttribute("captchaText"); // 驗證成功後從Session中移除

        if (shopService.existsByName(shopRequest.getName())) {
            errorResponse.setCode(NewErrorStatus.SHOP_DUPLICATE_NAME.getKey());
            errorResponse.setMessage(NewErrorStatus.SHOP_DUPLICATE_NAME.getChinese());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
        User user = userService.findById(customUserDetails.getId());
        Shop shop = shopService.addShop(shopRequest, user);

        return ResponseEntity.ok().body(shop.getId());
    }

}
