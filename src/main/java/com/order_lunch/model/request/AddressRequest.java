package com.order_lunch.model.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class AddressRequest {

    private Integer id;

    @NotBlank
    private String city;

    @NotBlank
    private String area;

    @NotBlank
    private String street;

    @NotBlank
    @Size(max = 255)
    private String detail;

}