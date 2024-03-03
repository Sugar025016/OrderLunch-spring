package com.order_lunch.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.order_lunch.model.request.AddMealsRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Entity
@Table(name = "add_meals")
public class AddMeals {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GenericGenerator(name = "auto_increment", strategy = "native")
    @Column(name = "id")
    private Integer id;

    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "addMeals", fetch = FetchType.LAZY)
    private List<AddMealsDetail> addMealsDetails;

    @JsonIgnore
    @JoinColumn(name = "shop_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Shop shop;

    @Column(name = "is_shelve", length = 255, nullable = false)
    private boolean isShelve;

    @Override
    public String toString() {
        return "Tab{" +
                "id=" + id +
                ", name=" + name +
                ", isShelve=" + isShelve +
                '}';
    }

    public AddMeals(Shop shop, AddMealsRequest addMealsRequest) {
        this.name = addMealsRequest.getName();
        this.isShelve = addMealsRequest.isShelve();
        this.shop = shop;
    }

}
