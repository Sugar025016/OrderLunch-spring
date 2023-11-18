package com.order_lunch.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
// import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Repository;

import com.order_lunch.entity.Order;
import com.order_lunch.entity.Shop;
import com.order_lunch.entity.User;


@Repository
public interface IOrderRepository extends JpaRepository<Order, Integer> {

    Page<Order> findByUserAndStatusNotIn(User user,List<Integer> inOrder ,Pageable pageable);

    Page<Order> findByUser(User user ,Pageable pageable);

	// @Query("SELECT o FROM Order o WHERE o.status < 10 ORDER BY CASE WHEN o.status = 11 THEN 0 WHEN o.status = 12 THEN 0 ELSE 1 END, o.takeTime DESC")
    @Query("SELECT o FROM Order o WHERE o.user=:user ORDER BY CASE WHEN o.status = 11 THEN 0 WHEN o.status = 12 THEN 0 ELSE 1 END, o.takeTime DESC")
    Page<Order> getOrderByUserPage(@Param("user") User user,Pageable pageable);

    Page<Order> getOrderByShopAndStatusIn(Shop orElseThrow,List<Integer> status, Pageable pageable);


    List<Order> getOrderByShopAndIdInAndStatusIn(Shop shop,List<Integer> orderIds ,List<Integer> status);

    // Page<Order> getOrderByShopPageAndStatusMin(Shop orElseThrow,int 10,Pageable pageable);
}