package com.order_lunch.model.request;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartRequest {


    @NotNull
    private Integer productId;

    private String department;

    @NotNull
    private String orderUsername;

    @NotNull
    private Integer qty;

    private String remark;
    

    
}
