package com.order_lunch.model.response;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.order_lunch.entity.AddMeals;
import com.order_lunch.entity.AddMealsDetail;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class AddMealsResponse {

    private String name;
    private int id;

    @JsonProperty("shelve")
    private boolean isShelve = true;

    @JsonProperty("products")
    private List<AddProduct> addProducts;

    public AddMealsResponse(AddMeals addMeals) {
        this.name = addMeals.getName();
        this.id = addMeals.getId();

        int shopId = addMeals.getShop().getId();

        // this.addProducts =
        // addMeals.getAddMealsDetails().stream().map(AddProduct::new).collect(Collectors.toList());
        this.addProducts = addMeals.getAddMealsDetails().stream().map(v -> new AddProduct(v, shopId))
                .collect(Collectors.toList());

        // this.addProducts = addMeals.getAddMealsDetails().stream().map(v->{
        // return new AddProduct(v);
        // }).collect(Collectors.toList());

    }

    public static class AddProduct {

        @JsonProperty("id")
        private Integer productId;
        private Integer price;
        private ProductResponse product;

        public AddProduct(AddMealsDetail addMealsDetail, int shopId) {
            BeanUtils.copyProperties(addMealsDetail, this);
            this.productId = addMealsDetail.getId();
            this.price = addMealsDetail.getPrice();
            this.product = new ProductResponse(addMealsDetail.getProduct(), shopId);

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

        public ProductResponse getProduct() {
            return product;
        }

        public void setProduct(ProductResponse product) {
            this.product = product;
        }

    }

}