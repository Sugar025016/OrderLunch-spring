package com.order_lunch.model.request;

import java.time.LocalDateTime;

import javax.validation.constraints.Size;

import com.order_lunch.validator.DateTimeAfter;

import lombok.NonNull;

// @Data
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

    public LocalDateTime getTakeTime() {
        return takeTime;
    }

    public void setTakeTime(LocalDateTime takeTime) {
        this.takeTime = takeTime;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
    
    
}
