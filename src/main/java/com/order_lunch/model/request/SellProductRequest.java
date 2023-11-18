package com.order_lunch.model.request;

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
public class SellProductRequest {


    private Integer id;

    private String name;

    @Size(min=8,max=255)
    private String description;
    
    private Integer prise;

    @JsonProperty("orderable")
    private boolean isOrderable;
    
    private Integer imgId;
    
    private String imgUrl;
    

}

