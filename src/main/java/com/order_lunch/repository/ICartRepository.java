package com.order_lunch.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.order_lunch.entity.Cart;

@Repository
public interface ICartRepository extends JpaRepository<Cart,Integer>{
    
    List<Cart> getAllByUser_id(int id);

    Optional<Cart> findByIdAndUser_id(Integer id, int userId);

    Optional<Cart> getByIdAndUser_id(int cartId, int id);


    long countByUser_id(int id);

    @Modifying
    @Query(value = "DELETE FROM Cart c WHERE c.id in ?1")
    int deleteAll(List<Integer> ids);

    @Modifying
    @Query(value = "DELETE FROM Cart c WHERE c.user.id = ?1")
    int deleteAllByUserId(Integer id);

}
