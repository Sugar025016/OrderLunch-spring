package com.order_lunch.model.request;

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
public class UserRequest {

    // private Integer id;

    @NotNull(message = "name 不能為空")
    @Size(min=3,max=16)
    private String name;

    // // @NotNull(message = "phone 不能為空")
    // @Size(min=10,max=11)
    // private String phone;

    // @NotNull(message = "account 不能為空")
    @Size(min=4,max=16)
    private String account;

    // @NotNull(message = "password 不能為空")
    @Size(min=8,max=16)
    private String password;


    @JsonProperty("roleName")
    @NotNull(message = "role 不能為空")
    @Size(min=3,max=5)
    private String role;

    // @Email
    // private String email;
}
