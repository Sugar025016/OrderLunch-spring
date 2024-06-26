package com.order_lunch.model.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class AddressResponse {

    private Integer id;

    private String city;

    private String area;

    private String detail;

}