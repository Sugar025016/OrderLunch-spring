package com.order_lunch.service;

import java.util.Set;

import com.order_lunch.entity.Tab;
import com.order_lunch.model.request.TabProductRequest;

public interface ITabService {
    Set<Tab> findTabByShopId(int id);

    boolean setTabByShopId(int tabId, TabProductRequest tabProductRequest, int userId);

    boolean addTabByShopId( TabProductRequest tabProductRequest, int userId);
    boolean deleteTab(int tabId, int userId) ;
}
