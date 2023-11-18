package com.order_lunch.model.request;

import java.util.List;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TabProductRequest {

    private Integer id;
    private Integer shopId;

    @NotBlank(message = "不能為空")
    private String name;


    @JsonProperty("shelve")
    private boolean isShelve;

    private List<Integer> productIds;


}