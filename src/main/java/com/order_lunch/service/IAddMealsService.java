package com.order_lunch.service;

import java.util.Set;

import com.order_lunch.entity.AddMeals;
import com.order_lunch.model.request.AddMealsRequest;

public interface IAddMealsService {


    Set<AddMeals> getAddMeals(int userId,int shopId);

    boolean putAddMeals(int userId,int addMealsId, AddMealsRequest AddMealsProductRequest);

    boolean addAddMealsByShopId(int userId,AddMealsRequest addMealsProductRequest);

    boolean deleteAddMeals(int userId,int addMealsId);

}
