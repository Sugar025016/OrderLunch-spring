package com.order_lunch.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Entity
@Table(name = "address_data")
public class AddressData{
    
    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "city")
    private String city;

    @Column(name = "area")
    private String area;

    @Column(name = "street")
    private String street;

    @OneToMany(mappedBy = "addressData", cascade = CascadeType.DETACH)
    private List<Address> addresses;

    public AddressData(Integer id, String city, String area, String street) {
        this.id = id;
        this.city = city;
        this.area = area;
        this.street = street;
    }

}
