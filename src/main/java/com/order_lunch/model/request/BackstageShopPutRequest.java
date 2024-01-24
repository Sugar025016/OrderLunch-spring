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
public class BackstageShopPutRequest {

    @NotNull
    private Integer id;


    @JsonProperty("name")
    private String shopName;

    @Size(min=8,max=255)
    private String description;

    private AddressResponse address;

    @Size(min=10,max=11)
    private String phone;

    private Integer imgId;

    @NotNull
    private String imgUrl;
    


    @JsonProperty("orderable")
    private boolean isOrderable;
    @JsonProperty("disable")
    private boolean isDisable;
    private boolean isDelete;
}
