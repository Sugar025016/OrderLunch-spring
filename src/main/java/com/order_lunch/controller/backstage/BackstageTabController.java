package com.order_lunch.controller.backstage;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.order_lunch.entity.Shop;
import com.order_lunch.model.response.BackstageShopNameResponse;
import com.order_lunch.service.Impl.ShopService;

@RestController
@RequestMapping("/backstage/tab")
public class BackstageTabController {
    @Autowired
    ShopService shopService;

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<BackstageShopNameResponse> getShop(@PathVariable() int id) {
        Shop shopById = shopService.getShopById(id);
        BackstageShopNameResponse backstageShopNameResponse = new BackstageShopNameResponse(shopById);

        return ResponseEntity.ok().body(backstageShopNameResponse);
    }

    @RequestMapping(path = "/name/{name}", method = RequestMethod.GET)
    public ResponseEntity<List<BackstageShopNameResponse>> getShop(@PathVariable() String name) {
        List<Shop> findShopsByName = shopService.findShopsByName(name);
        List<BackstageShopNameResponse> collect = findShopsByName.stream().map(v -> new BackstageShopNameResponse(v))
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(collect);
    }
}
