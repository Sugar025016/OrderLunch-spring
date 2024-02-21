package com.order_lunch.controller.sell;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.order_lunch.config.CustomUserDetails;
import com.order_lunch.entity.Shop;
import com.order_lunch.model.request.BackstageShopAddRequest;
import com.order_lunch.model.request.BackstageShopPutRequest;
import com.order_lunch.model.response.SellShopResponse;
import com.order_lunch.model.response.ShopNameItemResponse;
import com.order_lunch.service.Impl.ShopService;

@RestController
@RequestMapping("/sell/shop")
public class SellShopController {

    @Autowired
    ShopService shopService;


    @RequestMapping(path = "", method = RequestMethod.GET)
    public ResponseEntity<List<ShopNameItemResponse>> getShopsName(@AuthenticationPrincipal CustomUserDetails customUserDetails ) {
        List<Shop> findShops = shopService.getShopsByUserId(customUserDetails.getId());
        List<ShopNameItemResponse> collect = findShops.stream().map(v->new ShopNameItemResponse(v)).collect(Collectors.toList());
        return ResponseEntity.ok().body(collect);
    }


    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<SellShopResponse> getShop(@PathVariable() int id,@AuthenticationPrincipal CustomUserDetails customUserDetails ) {
        SellShopResponse sellShopResponse = new SellShopResponse(shopService.getShopByUserId(customUserDetails.getId(), id));
        return ResponseEntity.ok().body(sellShopResponse);
    }

    @RequestMapping(path = "", method = RequestMethod.PUT)
    public ResponseEntity<Boolean> putShop(@RequestBody BackstageShopPutRequest shopPutRequest) {
        return ResponseEntity.ok().body(shopService.putShop(shopPutRequest));
    }

        @RequestMapping(path = "", method = RequestMethod.POST)
    public ResponseEntity<Boolean> postShop(@RequestBody BackstageShopAddRequest shopPutRequest) {
        return ResponseEntity.ok().body(shopService.addShop(shopPutRequest));
    }

}
