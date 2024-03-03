package com.order_lunch.repository;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.order_lunch.entity.AddMeals;

@Repository
public interface IAddMealsRepository extends JpaRepository<AddMeals, Integer> {


    @Modifying
    void deleteAllById(int addMealsId);


    @Query(value = "DELETE FROM add_meals_detail WHERE add_meals_id = :addMealsId", nativeQuery = true)
    Set<AddMeals> deleteByAddMealsId(int addMealsId);

    boolean existsByIdAndShopUserId(int addMealsId, int userId);

    Optional<AddMeals> findByIdAndShopUserId(int addMealsId, int userId);

    Set<AddMeals> findByShopIdAndShopUserId(int shopId, int userId);

}
