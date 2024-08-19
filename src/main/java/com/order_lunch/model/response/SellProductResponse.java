package com.order_lunch.model.response;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;

import com.order_lunch.entity.Product;

import lombok.Data;

@Data
public class SellProductResponse {

    private Integer id;
    // @JsonProperty("productName")
    private String name;
    private String description;
    private Integer price;

    private boolean isOrderable;

    private String imgUrl;
    private Integer imgId;
    private List<Integer> addMealsIdList;

    public SellProductResponse(Product product) {
        BeanUtils.copyProperties(product, this);
        this.addMealsIdList = product.getAddMealsList().stream().map(v -> v.getId()).collect(Collectors.toList());
        if (product.getFileData() != null) {
            this.imgUrl = product.getFileData().getFileName();
            this.imgId = product.getFileData().getId();
        }
    }


    public SellProductResponse(Product product,int shopId) {
        BeanUtils.copyProperties(product, this);
        this.addMealsIdList = product.getAddMealsList().stream().map(v -> v.getId()).collect(Collectors.toList());
        if (product.getFileData() != null) {
            this.imgUrl = product.getFileData().getFileName();
            this.imgId = product.getFileData().getId();
        }
    }
}
