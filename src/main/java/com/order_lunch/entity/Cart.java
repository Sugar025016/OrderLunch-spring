package com.order_lunch.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.beans.BeanUtils;

import com.order_lunch.model.request.CartRequest;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Entity
@Table(name = "cart")
public class Cart extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GenericGenerator(name = "auto_increment", strategy = "native")
    @Column(name = "id")
    private Integer id;

    @JoinColumn(name = "product_id")
    @ManyToOne()
    private Product product;


    @Column(name = "department", nullable = true)
    private String department;

    @Column(name = "order_username", nullable = true)
    private String orderUsername;

    @Column(name = "qty", nullable = true)
    private int qty;

    @Column(name = "remark", nullable = true)
    private String remark;

    @JsonIgnore
    @JoinColumn(name = "user_id")
    @ManyToOne()
    private User user;

    public Cart(CartRequest cartRequest, User user,Product product) {
        BeanUtils.copyProperties(cartRequest, this);
        this.user=user;
        this.product=product;

    }

    public void setCart(CartRequest cartRequest) {
        BeanUtils.copyProperties(cartRequest, this);
    }

}
