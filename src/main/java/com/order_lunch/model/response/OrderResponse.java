package com.order_lunch.model.response;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.order_lunch.entity.Address;
import com.order_lunch.entity.Order;
import com.order_lunch.entity.OrderDetail;
import com.order_lunch.entity.Shop;
import com.order_lunch.enums.Status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderResponse {


    private Integer orderId;
    private String shopName;
    private String userName;
    private String description;
    private String imgUrl;
    private int totalPrise;
    private String remark;
    private String statusChinese;
    private int status;
    private LocalDateTime orderTime;
    private LocalDateTime takeTime;
    private Address address;

    public OrderResponse(Order order) {
        BeanUtils.copyProperties(order,this);
        Shop shop = order.getShop();
        this.userName=order.getUser().getName();
        this.orderId=order.getId();
        this.shopName =shop.getName();
        this.description=shop.getDescription();
        List<OrderDetail> orderDetail = order.getOrderDetail();
        this.totalPrise = orderDetail.stream().mapToInt(v->v.getQty()*v.getPrise()).sum();
        this.orderTime=order.getCreateTime();
        this.status=  order.getStatus();
        this.statusChinese=  Status.getStatus(order.getStatus()).getChinese();
        
        if(shop.getFileData()!=null){
            this.imgUrl=shop.getFileData().getFileName();
        }
    }

}
