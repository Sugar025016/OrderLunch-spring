package com.order_lunch.entity;

import java.util.Objects;

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
import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.order_lunch.model.request.AddressRequest;
import com.order_lunch.model.request.AddressResponse;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Entity
@Table(name = "addr")
public class Address extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "auto_increment")
    @GenericGenerator(name = "auto_increment", strategy = "native")
    @Column(name = "id")
    private Integer id;

    // @Column(name = "city")
    // private String city;

    // @Column(name = "area")
    // private String area;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "address_data_id")
    private AddressData addressData;

    @Column(name = "detail")
    private String detail;

    @Column(name = "lat")
    private double lat = 0;

    @Column(name = "lng")
    private double lng = 0;



    @JsonIgnore
    @ManyToOne(cascade = CascadeType.DETACH,fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id" , nullable = false)
    private User user;

    // @JsonIgnore
    // @OneToOne(mappedBy = "addressDelivery", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    // private User userDelivery;


    // @JsonIgnore
    // @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    // private User loveUsers;

    @Override
    public String toString() {
        return "AddressData{" +
                "id=" + id +
                ", addressData=" + addressData +
                ", detail=" + detail +
                ", lat=" + lat +
                ", lng=" + lng +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(id, address.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public Address(AddressRequest addressRequest) {
        BeanUtils.copyProperties(addressRequest, this);
    }
    

    public void setAddress(AddressResponse addressRequest) {
        BeanUtils.copyProperties(addressRequest, this);
    }

    public Address(User user, AddressData addressData, String addressDetail , double lat,double lng) {
        this.user=user;
        this.addressData=addressData;
        this.detail=addressDetail;
        this.lat=lat;
        this.lng=lng;

    }


}
