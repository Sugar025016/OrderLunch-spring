package com.order_lunch.entity;

import java.time.LocalDateTime;
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
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Entity
@Table(name = "ordered")
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GenericGenerator(name = "auto_increment", strategy = "native")
    @Column(name = "id")
    private Integer id;

    @Column(name = "take_time", updatable = false)
    private LocalDateTime takeTime;

    @Column(name = "remark", nullable = true)
    private String remark;

    // @Column(name = "is_finish", nullable = true)
    // private int isFinish;

    @Column(name = "status", nullable = true)
    private int status=11;

    @JoinColumn(name = "shop_id")
    @ManyToOne(cascade = CascadeType.DETACH)
    private Shop shop;

    @JoinColumn(name = "user_id")
    @ManyToOne(cascade = CascadeType.DETACH)
    private User user;

    @JoinColumn(name = "address_id")
    @OneToOne(cascade = CascadeType.ALL)
    private Address address;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "order",fetch = FetchType.LAZY)
    // @OneToMany(cascade = CascadeType.ALL)
    private List<OrderDetail> orderDetail;

    public Order(LocalDateTime takeTime, String remark, Shop shop, User user, Address address) {
        this.takeTime = takeTime;
        this.remark = remark;
        this.shop = shop;
        this.user = user;
        this.address = address;
    }

}
