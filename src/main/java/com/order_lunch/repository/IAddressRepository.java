package com.order_lunch.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.order_lunch.entity.Address;

public interface IAddressRepository extends JpaRepository<Address,Integer> {


    
}
