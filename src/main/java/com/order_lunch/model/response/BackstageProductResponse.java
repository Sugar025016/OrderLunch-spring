package com.order_lunch.model.response;

import java.time.LocalDateTime;

import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.order_lunch.entity.Product;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BackstageProductResponse {

    private Integer shopId;
    private String shopName;

    private Integer id;
    @JsonProperty("productName")
    private String name;
    private String description;
    private Integer price;

    private boolean isDelete;
    private boolean isOrderable;

    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    private String imgUrl;
    private Integer imgId;

    public BackstageProductResponse(Product product) {
        BeanUtils.copyProperties(product, this);
        // Tab tab = product.getTab();
        this.shopId=product.getShop().getId();
        this.shopName=product.getShop().getName();
        if (product.getFileData() != null) {
            this.imgUrl = product.getFileData().getFileName();
            this.imgId = product.getFileData().getId();
        }
    }

}
