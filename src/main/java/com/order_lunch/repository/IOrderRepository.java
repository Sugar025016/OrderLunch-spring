package com.order_lunch.repository;

import java.util.List;
import java.util.Set;

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

    Page<Order> findByUserAndStatusNotIn(User user, List<Integer> inOrder, Pageable pageable);

    Page<Order> findByUser(User user, Pageable pageable);

    // @Query("SELECT o FROM Order o WHERE o.user=:user o.status in :status ORDER BY
    // o.create_time DESC")
    // Page<Order> getOrderByUserStatesPage(@Param("user") User user, List<Integer>
    // status, Pageable pageable);

    // @Query("SELECT o FROM Order o WHERE o.user = :user AND o.status IN :status
    // ORDER BY o.create_time DESC")
    // Page<Order> getOrderByUserStatesPage(@Param("user") User user,
    // @Param("status") List<Integer> status, Pageable pageable);

    // @Query("SELECT o FROM Order o WHERE o.user = :user AND o.status IN :status
    // ORDER BY o.create_time DESC")
    // Page<Order> getOrderByUserAndStatusIn(User user, List<Integer> status, Pageable pageable);

    // @Query("SELECT o FROM Order o WHERE o.user=:user o.status in :status ORDER BY
    // o.take_time DESC")
    // List<Order> getOrderByUserStates(@Param("user") User user, List<Integer>
    // status);

    @Query("SELECT o FROM Order o WHERE o.user = :user AND o.status IN :status ORDER BY o.takeTime DESC")
    List<Order> getOrderByUserStates(@Param("user") User user, @Param("status") List<Integer> status);

    // // @Query("SELECT o FROM Order o WHERE o.status < 10 ORDER BY CASE WHEN
    // o.status = 11 THEN 0 WHEN o.status = 12 THEN 0 ELSE 1 END, o.takeTime DESC")

    // @Query("SELECT o FROM Order o WHERE o.user=:user ORDER BY CASE WHEN o.status = 11 THEN 0 WHEN o.status = 12 THEN 0 ELSE 1 END, o.takeTime DESC")
    // Page<Order> getOrderByUserPage(@Param("user") User user,Pageable pageable);


    @Query("SELECT o FROM Order o WHERE o.user=:user ORDER BY CASE WHEN o.status NOT IN :status THEN 0 ELSE 1 END, o.takeTime DESC")
    Page<Order> getOrderByUser(@Param("user") User user, List<Integer> status,Pageable pageable);


    Page<Order> getOrderByShopAndStatusIn(Shop orElseThrow, List<Integer> status, Pageable pageable);

    List<Order> getOrderByShopAndIdInAndStatusIn(Shop shop, List<Integer> orderIds, Set<Integer> status);

    List<Order> getOrderByShopUserAndStatus(User user, int status);
    // Page<Order> getOrderByShopPageAndStatusMin(Shop orElseThrow,int 10,Pageable
    // pageable);

    // List<Order> getOrderByOrderIdIn(List<Integer> orderIds);

    List<Order> getOrderByIdIn(List<Integer> orderIds);
}