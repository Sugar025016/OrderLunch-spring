package com.order_lunch.model.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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

    @NotNull
    @NotEmpty(message = "name 不能為空")
    @Size(min=3,max=32)
    private String name;

    // // @NotNull(message = "phone 不能為空")
    // @Size(min=10,max=11)
    // private String phone;

    @NotNull(message = "account 不能為空")
    @Size(min=4,max=64)
    private String account;

    @NotNull(message = "password 不能為空")
    @Size(min=8,max=16)
    private String password;

    @NotNull(message = "password 不能為空")
    @NotBlank(message = "密碼不能為空")
    @Size(min=8,max=16)
    private String passwordCheck;



    @NotBlank(message = "驗證碼不能為空")
    @Size(min = 4, max = 4, message = "驗證碼的長度應為4")
    private String verifyCode;



    

    // @JsonProperty("roleName")
    // @Size(min=3,max=5)
    // private String role;

    // @Email
    // private String email;
}
