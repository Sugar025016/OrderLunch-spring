package com.order_lunch.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.order_lunch.model.request.BackstageShopAddRequest;
import com.order_lunch.model.request.BackstageShopPutRequest;
import com.order_lunch.model.request.ShopSearchRequest;
import com.order_lunch.model.response.ShopDetailResponse;
import com.order_lunch.model.response.ShopResponse;
import com.order_lunch.service.Impl.ShopService;

@RestController
@RequestMapping("/api/shop")
public class ShopController {

    @Autowired
    ShopService shopService;

    // @RequestMapping(path = "", method = RequestMethod.GET)
    // public ResponseEntity<List<ShopResponse>> getShops(ShopSearchRequest
    // shopRequest) {

    // Set<Shop> findShops = shopService.findShops(shopRequest);
    // List<ShopResponse> arrayList = new ArrayList<>();
    // for (Shop findShop : findShops) {
    // ShopResponse shopResponse = new ShopResponse(
    // findShop);
    // arrayList.add( shopResponse);
    // }
    // return ResponseEntity.ok().body(arrayList);
    // }

    @RequestMapping(path = "", method = RequestMethod.GET)
    public ResponseEntity<Page<ShopResponse>> getShops(ShopSearchRequest shopRequest,
            @PageableDefault(page = 0, size = 5) Pageable pageable) {
        Page<ShopResponse> findShops = shopService.findShops(shopRequest, pageable);
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

    @RequestMapping(path = "", method = RequestMethod.PUT)
    public ResponseEntity<Boolean> putShop(@RequestBody BackstageShopPutRequest shopPutRequest) {
        return ResponseEntity.ok().body(shopService.putShop(shopPutRequest));
    }

    @RequestMapping(path = "", method = RequestMethod.POST)
    public ResponseEntity<Boolean> postShop(@RequestBody BackstageShopAddRequest shopPutRequest) {
        return ResponseEntity.ok().body(shopService.addShop(shopPutRequest));
    }

}