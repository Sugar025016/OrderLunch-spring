package com.order_lunch.service;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.order_lunch.entity.Shop;
import com.order_lunch.model.request.BackstageShopAddRequest;
import com.order_lunch.model.request.BackstageShopPutRequest;
import com.order_lunch.model.request.ShopRequest;
import com.order_lunch.model.request.ShopSearchRequest;
import com.order_lunch.model.response.BackstageShopResponse;

public interface IShopService {
    // List<Shop> getShops();
    Set<Shop> findShops(ShopSearchRequest shopRequest);
    Shop getShopById(int id);
    Page<BackstageShopResponse> findShops(ShopSearchRequest shopRequest, Pageable pageable);
    boolean addShop(ShopRequest shopRequest);
    // boolean existsById(int id);
    boolean putShop(BackstageShopPutRequest shopPutRequest);
    boolean addShop(BackstageShopAddRequest ShopAddRequest);
    Set<Shop> findShopsLim();
    List<Shop> findShopsByName(String name);
    List<Shop> getShopsByUserId(int id);
    Shop getShopByUserId(int shopId,int userId);
    boolean deleteShop(int id);
    
}
