package com.order_lunch.model.response;

import org.springframework.beans.BeanUtils;

import com.order_lunch.entity.Product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductResponse {

    private Integer id;

    private String name;
    private String description;
    private String imgUrl;
    private boolean isOrderable;
    private int price;
    private int shopId;

    // private int total;

    // private int totalOriginPrice;

    public ProductResponse(Product product, int shopId) {
        BeanUtils.copyProperties(product, this);
        this.shopId = shopId;

        if (product.getFileData() != null) {
            this.imgUrl = product.getFileData().getFileName();
        }

    }

}
