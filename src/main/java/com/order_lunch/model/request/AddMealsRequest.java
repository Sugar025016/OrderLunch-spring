package com.order_lunch.model.request;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AddMealsRequest {


    @NonNull
    private Integer shopId;

    @NotBlank(message = "不能為空")
    private String name;

    @JsonProperty("shelve")
    private boolean isShelve = true;

    @JsonProperty("products")
    private List<AddProduct> addProducts;


    public List<Integer> getProductIds() {
        return addProducts.stream()
                          .map(AddProduct::getProductId).collect(Collectors.toList());
    }

    public static class AddProduct {

        @NonNull
        @JsonProperty("id")
        private Integer productId;
        @NonNull
        private Integer price;

        public AddProduct() {
        }

        public AddProduct(@NonNull Integer productId, @NonNull Integer price) {
            this.productId = productId;
            this.price = price;
        }

        public Integer getProductId() {
            return productId;
        }

        public void setProductId(Integer productId) {
            this.productId = productId;
        }

        public Integer getPrice() {
            return price;
        }

        public void setPrice(Integer price) {
            this.price = price;
        }

    }

}