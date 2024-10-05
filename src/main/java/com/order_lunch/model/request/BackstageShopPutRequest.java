package com.order_lunch.model.request;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BackstageShopPutRequest {

    @NotNull
    private Integer id;

    @JsonProperty("name")
    @Size(min = 3, max = 16)
    @NotBlank
    private String shopName;

    @Size( max = 255)
    private String description;

    @NotBlank
    @Size(min = 10, max = 11)
    private String phone;

    private Integer imgId;

    // @NotNull
    private String imgUrl;

    @NotNull
    private Double deliveryKm;

    @NotNull
    private Integer deliveryPrice;

    @Valid
    @NotNull
    private AddressRequest address;

    @NotNull
    @JsonProperty("orderable")
    private Boolean isOrderable;

    @NotNull
    @JsonProperty("open")
    private Boolean isOpen;

    private boolean isDelete;
}
