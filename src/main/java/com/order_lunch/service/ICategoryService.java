package com.order_lunch.service;

import java.util.List;

import com.order_lunch.entity.Category;

public interface ICategoryService {
    List<Category> findAll();    
    Category getCategoryById(int id);
    boolean deleteById(int id); 
    boolean existsById(int id) ;
}
