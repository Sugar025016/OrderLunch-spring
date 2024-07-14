package com.order_lunch.service.Impl;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.order_lunch.entity.Product;
import com.order_lunch.entity.Shop;
import com.order_lunch.entity.Tab;
import com.order_lunch.model.request.TabProductRequest;
import com.order_lunch.repository.ITabRepository;
import com.order_lunch.service.IShopService;
import com.order_lunch.service.ITabService;
import com.order_lunch.service.IUserService;

@Service
public class TabService implements ITabService {
    @Autowired
    ITabRepository iTabRepository;
    @Autowired
    IUserService iUserService;
    @Autowired
    IShopService iShopService;

    @Override
    public Set<Tab> findTabByShopId(int id) {

        // return iTabRepository.findByShopIdAndProductsIsOrderableIsTrue(id);
        Set<Tab> findByIsShelveIsTrueAndShopId = iTabRepository.findByIsShelveIsTrueAndShopId(id);
        findByIsShelveIsTrueAndShopId.stream().forEach(t -> {
            List<Product> products = t.getProducts().stream().filter(p -> p.isOrderable()).collect(Collectors.toList());
            t.setProducts(products);
        });
        return findByIsShelveIsTrueAndShopId;
    }

    @Override
    public boolean addTabByShopId(TabProductRequest tabProductRequest, int userId) {
        Shop shopByUserId = iShopService.getShop(userId, tabProductRequest.getShopId());
        List<Product> products = tabProductRequest.getProductIds().stream().map(pId -> {
            Optional<Product> optional = shopByUserId.getProducts().stream().filter(p -> p.getId() == pId).findFirst();
            Product orElseThrow = optional.orElseThrow(() -> new IllegalArgumentException("Value not found"));
            return orElseThrow;
        }).collect(Collectors.toList());

        Tab tab = new Tab(tabProductRequest.getName(), shopByUserId, tabProductRequest.isShelve(), products);
        Tab save = iTabRepository.save(tab);
        return save != null;
    }

    @Override
    public boolean setTabByShopId(int tabId, TabProductRequest tabProductRequest, int userId) {

        Shop shopByUserId = iShopService.getShop(userId, tabProductRequest.getShopId());
        Optional<Tab> findFirst = shopByUserId.getTabs().stream().filter(v -> v.getId() == tabId).findFirst();
        Tab tab = findFirst.orElseThrow(() -> new IllegalArgumentException("Value not found"));

        List<Product> products = tabProductRequest.getProductIds().stream().map(pId -> {
            Optional<Product> optional = shopByUserId.getProducts().stream().filter(p -> p.getId() == pId).findFirst();
            Product orElseThrow = optional.orElseThrow(() -> new IllegalArgumentException("Value not found"));
            return orElseThrow;
        }).collect(Collectors.toList());

        tab.setName(tabProductRequest.getName());
        tab.setShelve(tabProductRequest.isShelve());
        tab.setProducts(products);

        Tab save = iTabRepository.save(tab);

        return save != null;
    }

    @Override
    public boolean deleteTab(int tabId, int userId) {

        Tab orElseThrow = iTabRepository.getByIdAndShopUserId(tabId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Value not found"));

        Shop shop = orElseThrow.getShop();
        shop.getTabs().remove(orElseThrow);
        iTabRepository.delete(orElseThrow);

        return !iTabRepository.findById(tabId).isPresent();
    }

}
