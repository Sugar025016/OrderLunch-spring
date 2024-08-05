package com.order_lunch.model.response;

import org.springframework.beans.BeanUtils;

import com.order_lunch.entity.OrderDetail;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderDetailResponse {


    private Integer orderDetailId;
    private Integer productId;
    private String productName;

    // @JsonProperty("aaaa")
    private int qty;

    private int price;
    private String remark;
    private int status;

    private String imgUrl;

    // private int total;

    // private int totalOriginPrice;

    public OrderDetailResponse(OrderDetail orderDetail) {
        BeanUtils.copyProperties(orderDetail,this);
        this.orderDetailId=orderDetail.getId();
        this.productId=orderDetail.getProduct().getId();
        this.productName = orderDetail.getProduct().getName();
        if(orderDetail.getProduct().getFileData()!=null){
            this.imgUrl=orderDetail.getProduct().getFileData().getFileName();
        }
    }

}
