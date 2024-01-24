package com.order_lunch.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.order_lunch.entity.User;
import com.order_lunch.model.response.BackstageUserResponse;


// @Repository
public interface IUserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByAccount(String account);

    User getByAccount(String account);

    @Query(value = "SELECT NEW com.order_lunch.model.response.BackstageUserResponse(u) FROM User u " +
            "WHERE (:name IS NULL OR u.name LIKE %:name%)" +
            "group by u.id", countQuery = "SELECT COUNT(u) FROM User u " +
                    "WHERE (:name IS NULL OR u.name LIKE %:name%) " +
                    "group by u.id")
    Page<BackstageUserResponse> findUsersByName(@Param("name") String name, Pageable pageable);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.account = :account")
    boolean existByAccount(String account);

    // List<Shop> findByShopLoveList_userId(int id);

    List<User> findFirst6ByAccountLike(String username);
    List<User> findFirst6ByAccountContaining(String username);

    boolean existsByAccount(String account);
    


}