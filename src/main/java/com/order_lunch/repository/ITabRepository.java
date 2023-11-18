package com.order_lunch.repository;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import com.order_lunch.entity.Tab;

@Repository
public interface ITabRepository extends  JpaRepository<Tab,Integer>  {

    Set<Tab> findByShopIdAndProductsIsOrderableIsTrue(int id);
    Set<Tab> findByIsShelveIsTrueAndShopId(int id);
    
    Optional<Tab> findTabByIdAndShopIdAndShopUserId(int tabId,int shopId,int userId);

    // List<Tab> findAllByShopIdAndProductsIsOrderableIsTrue(int id);
    // List<Tab> findAllByShopIdAndProductsOrederableIsTrue(int id);
    
    // List<Tab> findAllByShopId(int id);

    @Modifying
    void deleteById(int id);

    Optional<Tab> getByIdAndShopUserId(int tabId, int userId);
}
