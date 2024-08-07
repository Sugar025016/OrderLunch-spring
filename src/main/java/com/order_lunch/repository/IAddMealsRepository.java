package com.order_lunch.repository;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import com.order_lunch.entity.AddMeals;

@Repository
public interface IAddMealsRepository extends JpaRepository<AddMeals, Integer> {


    @Modifying
    void deleteAllById(int addMealsId);


    // @Query(value = "DELETE FROM add_meals_detail WHERE add_meals_id = ?1", nativeQuery = true)
    // int deleteByAddMealsId(int addMealsId);

    // @Query(value = "DELETE FROM add_meals_detail WHERE add_meals_id = ?1", nativeQuery = true)
    // void deleteById(int addMealsId);

    boolean existsByIdAndShopUserId(int addMealsId, int userId);

    Optional<AddMeals> findByIdAndShopUserId(int addMealsId, int userId);

    Set<AddMeals> findByShopIdAndShopUserId(int shopId, int userId);

}
