package com.order_lunch.model.request;

import java.time.LocalDateTime;

import javax.validation.constraints.Size;

import com.order_lunch.validator.DateTimeAfter;

import lombok.Data;
import lombok.NonNull;

@Data
public class OrderRequest {

    @NonNull
    @DateTimeAfter
    private LocalDateTime takeTime;

    private int addressId;

    @Size(max=255)
    private String remark;

    public OrderRequest(@NonNull LocalDateTime takeTime, int addressId, @Size(min = 8, max = 255) String remark) {
        this.takeTime = takeTime;
        this.addressId = addressId;
        this.remark = remark;
    }
    
}
