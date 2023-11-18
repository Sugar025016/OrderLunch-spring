package com.order_lunch.repository;

import java.util.List;
import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.order_lunch.entity.Product;
import com.order_lunch.model.response.BackstageProductResponse;

@Repository
public interface IProductRepository extends JpaRepository<Product, Integer> {

    Product getProductById(Integer id);

    // List<Product> getProductByShop_id(Integer id);

    List<Product> getProductByTab_shop_id(Integer id);

    // List<Product> getAllByShop_Id(Integer id);
    List<Product> getAllByTab_shop_id(Integer id);

    @Query(value = "SELECT NEW com.order_lunch.model.response.BackstageProductResponse(p) " +
            "FROM Product p LEFT JOIN p.tab t " +
            "LEFT JOIN t.shop s " +
            "WHERE (:shop_id IS NULL OR s.id = :shop_id) AND ( p.isDelete = false )", countQuery = "SELECT COUNT(p)" +
                    "FROM Product p LEFT JOIN p.tab t " +
                    "LEFT JOIN t.shop s " +
                    "WHERE (:shop_id IS NULL OR s.id = :shop_id) AND ( p.isDelete = false )")
    Page<BackstageProductResponse> findAllByShopIdAndDeleteIsFalse(@Param("shop_id") Integer shopId, Pageable pageable);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Product> findById(int id);

    Optional<Product> findByIdAndShopUserId(int productId, int userId);

    Optional<Product> findByIdAndShopUserIdAndIsDeleteIsFalse(int productId, int userId);

    List<Product> getProductByShopIdAndShopUserId(int shopId, int userId);

    List<Product> getProductByShopIdAndShopUserIdAndIsDeleteIsFalse(int shopId, int userId);
    
}