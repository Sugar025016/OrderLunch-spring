package com.order_lunch.model.request;

import java.util.List;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
// @Getter
// @Setter
public class TabProductRequest {

    private Integer id;
    private Integer shopId;

    @NotBlank(message = "不能為空")
    private String name;


    @JsonProperty("shelve")
    private boolean isShelve;

    private List<Integer> productIds;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isShelve() {
        return isShelve;
    }

    public void setShelve(boolean isShelve) {
        this.isShelve = isShelve;
    }

    public List<Integer> getProductIds() {
        return productIds;
    }

    public void setProductIds(List<Integer> productIds) {
        this.productIds = productIds;
    }




}