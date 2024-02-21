package com.order_lunch.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.order_lunch.entity.Address;


public interface IAddressRepository extends JpaRepository<Address, Integer> {

    Optional<Address> findByUserIdAndId(int userId, int addressId);

    boolean existsByIdAndUser_id(int addressId, int userId);

    // Optional<Address> findByUserIdAndAddressId(int userId, Integer id);

    // @Query("SELECT a FROM Address a "     +
    // "LEFT JOIN a.user u "+
    // "WHERE u.id= :userId"+
    // "ORDER BY CASE WHEN a.id = u.address.id THEN 0 ELSE 1 END")
    // List<Address> findByUserIdAndAddressDeliveryFirst(int userId);

}
