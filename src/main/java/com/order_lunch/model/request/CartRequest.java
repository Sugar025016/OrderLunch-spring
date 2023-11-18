package com.order_lunch.model.request;

import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartRequest {


    @NotEmpty
    private int productId;

    private String department;

    @NonNull
    private String orderUsername;

    @NotEmpty
    private int qty;

    private String remark;
    

    
}
