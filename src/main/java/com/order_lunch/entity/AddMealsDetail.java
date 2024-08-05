package com.order_lunch.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.order_lunch.model.request.AddMealsRequest.AddProduct;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Entity
@Table(name = "add_meals_detail")
public class AddMealsDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GenericGenerator(name = "auto_increment", strategy = "native")
    @Column(name = "id")
    private Integer id;

    @JsonIgnore
    @JoinColumn(name = "add_meals_id")
    @ManyToOne(cascade = CascadeType.DETACH,fetch = FetchType.LAZY)
    private AddMeals addMeals;

    @JsonIgnore
    @JoinColumn(name = "product_id")
    @ManyToOne(cascade = CascadeType.DETACH,fetch = FetchType.LAZY)
    private Product product;

    @Column(name = "price", length = 255, nullable = false)
    private int price;

    public AddMealsDetail(AddMeals addMeals, Product product, int price) {
        this.addMeals = addMeals;
        this.product = product;
        this.price = price;
    }

    public AddMealsDetail( Product product, int price) {
        this.product = product;
        this.price = price;
    }

    public AddMealsDetail(AddMeals addMeals,Product product,AddProduct addProduct) {
        this.addMeals = addMeals;
        this.product = product;
        this.price = addProduct.getPrice();
    }

    @Override
    public String toString() {
        return "Tab{" +
                "id=" + id +
                ", price=" + price +
                '}';
    }

}
