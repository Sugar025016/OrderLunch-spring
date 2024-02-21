package com.order_lunch.entity;

import javax.persistence.CascadeType;
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

import com.order_lunch.model.request.AddressRequest;
import com.order_lunch.model.request.AddressResponse;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Entity
@Table(name = "shop_address")
public class ShopAddress extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "auto_increment")
    @GenericGenerator(name = "auto_increment", strategy = "native")
    @Column(name = "id")
    private Integer id;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "address_data_id")
    private AddressData addressData;

    @Column(name = "detail")
    private String detail;

    @Column(name = "lat")
    private double lat = 0;

    @Column(name = "lng")
    private double lng = 0;


    public ShopAddress(@NonNull AddressRequest addressRequest) {
        BeanUtils.copyProperties(addressRequest, this);
    }
    
    public void setAddress(@NonNull AddressResponse addressRequest) {
        BeanUtils.copyProperties(addressRequest, this);
    }
    
    public ShopAddress(AddressData addressData, String addressDetail , double lat,double lng) {
        this.addressData=addressData;
        this.detail=addressDetail;
        this.lat=lat;
        this.lng=lng;
    }

}
