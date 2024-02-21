package com.order_lunch.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.order_lunch.entity.ShopAddress;

public interface IShopAddressRepository extends JpaRepository<ShopAddress,Integer> {

    // Optional<Address> findByUserIdAndAddressId(int userId, Integer id);


    
}
