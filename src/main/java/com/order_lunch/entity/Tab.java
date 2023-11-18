package com.order_lunch.entity;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Entity
@Table(name = "tab")
public class Tab {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GenericGenerator(name = "auto_increment", strategy = "native")
    @Column(name = "id")
    private Integer id;

    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @JsonIgnore
    @JoinColumn(name = "shop_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Shop shop;

    // @OneToMany(cascade = CascadeType.ALL, mappedBy = "tab")
    // private List<Product> products;
    @Column(name = "is_shelve", length = 255, nullable = false)
    private boolean isShelve;

    @ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinTable(name = "tab_product", joinColumns = @JoinColumn(name = "tab_id"), inverseJoinColumns = @JoinColumn(name = "product_id"), uniqueConstraints = @UniqueConstraint(columnNames = {
            "tab_id", "product_id" }))
    private List<Product> products;

    public Tab(String name, Shop shop, boolean isShelve, List<Product> products) {
        this.name = name;
        this.shop = shop;
        this.isShelve = isShelve;
        this.products = products;
    }

    // @JsonIgnore
    // @ManyToMany(cascade = CascadeType.ALL)
    // @JoinTable(name = "love", joinColumns = @JoinColumn(name = "user_id"),
    // inverseJoinColumns = @JoinColumn(name = "shop_id"), uniqueConstraints =
    // @UniqueConstraint(columnNames = {
    // "user_id", "shop_id" }))
    // @Where(clause = "is_delete = false")
    // private List<Shop> loves;

    public List<Product> getProductsForNotDelete() {
        return this.products.stream().filter(v -> !v.isDelete()).collect(Collectors.toList());
    }

}
