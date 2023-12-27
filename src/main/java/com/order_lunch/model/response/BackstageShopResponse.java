package com.order_lunch.model.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.order_lunch.entity.Shop;
import com.order_lunch.entity.Tab;
import com.order_lunch.model.AddressResponse;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BackstageShopResponse {


    private Integer id;

    private String userAccount;
    private String userName;

    @JsonProperty("shopName")
    private String name;
    private String phone;
    private String description;
    private String tabs;
    private AddressResponse address;
    private String imgUrl;
    private Integer imgId;
    private boolean isOrderable;
    private boolean isDisable;
    private boolean isDelete;

    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public BackstageShopResponse(Shop shop) {
        BeanUtils.copyProperties(shop, this);
        this.address=new AddressResponse(shop.getAddress());
        
        if (shop.getUser() != null) {
            this.userName = shop.getUser().getName();
            this.userAccount = shop.getUser().getAccount();
        }
        if (shop.getFileData() != null) {
            this.imgUrl = shop.getFileData().getFileName();
            this.imgId = shop.getFileData().getId();
        }
        List<Tab> tabsList = shop.getTabs();
        if(tabsList.size()!=0){
            List<String> collect = tabsList.stream().map(v -> v.getName()).collect(Collectors.toList());
            this.tabs=collect.toString();
        }


    }

}
