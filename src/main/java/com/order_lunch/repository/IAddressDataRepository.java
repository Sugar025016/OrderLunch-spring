package com.order_lunch.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.order_lunch.entity.AddressData;

public interface IAddressDataRepository extends JpaRepository<AddressData,Integer> {


    
}
