package com.order_lunch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import com.order_lunch.entity.AddMealsDetail;

@Repository
public interface IAddMealsDetailRepository extends JpaRepository<AddMealsDetail, Integer> {

    // Set<AddMeals> findByAddMeals_id(int addMealsId);

    @Modifying
    void deleteAllByAddMeals_id(int addMealsId);

//     @Modifying
// @Query(value = "DELETE FROM add_meals_detail WHERE addMeals_id IN :addMealsIds", nativeQuery = true)
// void deleteAllByAddMealsIdIn(@Param("addMealsIds") List<Integer> addMealsIds);


///////////////////////////***********************************************************////////////////////////////

    // @Transactional
    // @Modifying
    // @Query(value = "INSERT INTO add_meals_detail (addMeals_id, product_id, price)
    // VALUES (:addMealsId, :productId, :price)", nativeQuery = true)
    // void insertAddMealsDetails(List<Integer> addMealsId, List<Integer> productId,
    // List<Integer> price);

    // @Transactional
    // @Modifying
    // @Query(value = "INSERT INTO add_meals_detail (addMeals_id, product_id, price) VALUES (:addMealsId, :productId, :price)", nativeQuery = true)
    // List<AddMealsDetail> insertAddMealsDetails(int addMealsId, List<AddProduct> addProducts);

    // @Transactional
    // @Modifying
    // @Query(value = "INSERT INTO add_meals_detail (addMeals_id, product_id, price)
    // VALUES (:addMealsId, :productId, :price)", nativeQuery = true)
    // // List<AddMealsDetail> insertAddMealsDetails(@Param("addMealsId") int
    // addMealsId, @Param("productId") List<Integer> productId, @Param("price")
    // List<Integer> price);

    // List<AddMealsDetail> insertAddMealsDetails(@Param("addMealsId") int
    // addMealsId,
    // @Param("productId") List<Integer> productId,
    // @Param("price") List<Integer> price);

    // @Transactional
    // @Modifying
    // @Query(value = "INSERT INTO add_meals_detail (addMeals_id, product_id, price) VALUES (:addMealsId, :productId, :price)", nativeQuery = true)
    // List<AddMealsDetail> insertAddMealsDetails(@Param("addMealsId") int addMealsId, @Param("productId") List<Integer> productId,
    //         @Param("price") List<Integer> price);

    // @Transactional
    // @Modifying
    // @Query(value = "INSERT INTO add_meals_detail (addMeals_id, product_id, price) VALUES (:addMealsId, :productId, :price)", nativeQuery = true)
    // AddMealsDetail insertAddMealsDetails(@Param("addMealsId")int addMealsId,@Param("productId") int productId , @Param("price") int price);

    // @Transactional
    // @Modifying
    // @Query(value = "INSERT INTO add_meals_detail (addMeals_id, product_id, price) VALUES (:addMealsId, :productId, :price)", nativeQuery = true)
    // void insertAddMealsDetails(@Param("addMealsId")int addMealsId,@Param("productId") int productId , @Param("price") int price);


}
