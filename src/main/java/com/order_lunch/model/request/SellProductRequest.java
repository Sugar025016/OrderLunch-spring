package com.order_lunch.model.request;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SellProductRequest {

    // private Integer id;

    @NotBlank
    @Size(min = 3, max = 16)
    private String name;

    @Size(max = 255)
    private String description;

    @NotNull
    private Integer price;

    @JsonProperty("orderable")
    private boolean isOrderable;

    private Integer imgId;

    private String imgUrl;

    private List<Integer> addMealsIdList;

}
