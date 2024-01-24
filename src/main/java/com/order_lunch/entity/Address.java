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

import com.order_lunch.model.request.AddressResponse;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Entity
@Table(name = "address")
public class Address extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "auto_increment")
    @GenericGenerator(name = "auto_increment", strategy = "native")
    @Column(name = "id")
    private Integer id;

    @Column(name = "city")
    private String city;

    @Column(name = "area")
    private String area;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "address_data_id")
    private AddressData addressData;

    @Column(name = "detail")
    private String detail;

    @Column(name = "lat")
    private double lat = 0;

    @Column(name = "lng")
    private double lng = 0;

    public Address(AddressResponse addressRequest) {
        BeanUtils.copyProperties(addressRequest, this);
    }

    public void setAddress(AddressResponse addressRequest) {
        BeanUtils.copyProperties(addressRequest, this);
    }

    public Address(AddressData addressData, String addressDetail , double lat,double lng) {
        this.addressData=addressData;
        this.detail=addressDetail;
        this.lat=lat;
        this.lng=lng;

    }


}
