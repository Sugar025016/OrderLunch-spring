package com.order_lunch.entity;

import java.util.List;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Where;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.order_lunch.model.request.UserPutRequest;
import com.order_lunch.model.request.UserRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Entity
@Table(name = "user")
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GenericGenerator(name = "auto_increment", strategy = "native")
    @Column(name = "id")
    private Integer id;
    @Column(name = "name", length = 255, nullable = false)
    private String name;
    @Column(name = "phone", length = 11)
    private String phone;
    @Email
    @Column(name = "account", length = 64, nullable = false, unique = true)
    private String account;
    @Column(name = "password", length = 32, nullable = false)
    private String password;
    @Column(name = "role", length = 11, columnDefinition = "VARCHAR(11) DEFAULT 'user'")
    private String role;
    // @Email
    // @Column(name = "email", length = 255)
    // private String email;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "user_address", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "address_id"), uniqueConstraints = @UniqueConstraint(columnNames = {
            "user_id", "address_id" }))
    private List<Address> addresses;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private Address deliveryAddress;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "love", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "shop_id"), uniqueConstraints = @UniqueConstraint(columnNames = {
            "user_id", "shop_id" }))
    @Where(clause = "is_delete = false")
    private List<Shop> loves;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Cart> carts;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    @Where(clause = "is_delete = false")
    private List<Shop> shops;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Order> orders;

    // 給關聯過來的回傳值
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name=" + name +
                ", phone=" + phone +
                ", role=" + role +
                '}';
    }

    public User(UserRequest userRequest) {
        BeanUtils.copyProperties(userRequest, this);

    }

    public void setUser(UserPutRequest userPutRequest) {
        BeanUtils.copyProperties(userPutRequest, this);
    }

    public void setUser(UserRequest userRequest) {
        BeanUtils.copyProperties(userRequest, this);
    }

    public void setDeliveryAddress(Address address) {
        Optional<Address> findAny = addresses.stream().filter(v -> v.getId() == address.getId()).findAny();
        Address orElseThrow = findAny.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
            this.deliveryAddress = orElseThrow;
    }


    // public Set<ShopResponse> getShopLoveList() {
    // Set<ShopResponse> collect = shopLoveList.stream().map(v->new
    // ShopResponse(v)).collect(Collectors.toSet());
    // return collect;
    // }

    // public void setShopLoveList(Set<Shop> shopLoveList) {
    // this.shopLoveList = shopLoveList;
    // }

}
