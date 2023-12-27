package com.order_lunch.model.request;

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
public class BackstageShopAddRequest {

    @JsonProperty("userAccount")
    @NotNull(message = "account 不能為空")
    @Size(min=3,max=16)
    private String account;
    
    @JsonProperty("shopName")
    @NotNull(message = "name 不能為空")
    @Size(min=3,max=16)
    private String name;

    @Size(min=10,max=11)
    private String phone;

    @Size(min=8,max=255)
    private String description;

    private AddressResponse address;

    @NotNull
    private Integer imgId;
    
}
