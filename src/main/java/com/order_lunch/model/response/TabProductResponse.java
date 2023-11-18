package com.order_lunch.model.response;

import java.util.List;
import java.util.stream.Collectors;

import com.order_lunch.entity.Tab;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TabProductResponse {

    private Integer id;

    private String name;


    @JsonProperty("shelve")
    private boolean isShelve;

    private List<ProductResponse> products;

    public TabProductResponse(Tab tab) {

        this.id = tab.getId();
        this.name = tab.getName();
        this.isShelve = tab.isShelve();

        this.products = tab.getProductsForNotDelete().stream().map(v -> new ProductResponse(v))
        .collect(Collectors.toList());
    }

}
