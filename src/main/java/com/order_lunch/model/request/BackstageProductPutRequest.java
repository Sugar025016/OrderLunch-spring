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
public class BackstageProductPutRequest {

    private Integer shopId;
    
    private String shopName;

    @NotNull
    private Integer id;

    @JsonProperty("productName")
    private String name;

    @Size(min=8,max=255)
    private String description;
    
    private Integer prise;

    private boolean isOrderable;
    private boolean isDelete;
    
    private Integer imgId;

}
