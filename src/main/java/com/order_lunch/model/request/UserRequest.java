package com.order_lunch.model.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
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

    @NotBlank
    @Size(min = 3, max = 32)
    private String name;

    // // @NotNull(message = "phone 不能為空")
    // @Size(min=10,max=11)
    // private String phone;

    @NotBlank
    @Size(min = 4, max = 64)
    @Email
    private String account;

    @NotBlank
    @Size(min = 8, max = 16)
    private String password;

    @NotBlank
    @Size(min = 8, max = 16)
    private String passwordCheck;

    @NotBlank
    @Size(min = 4, max = 4)
    private String verifyCode;

    // @JsonProperty("roleName")
    // @Size(min=3,max=5)
    // private String role;

    // @Email
    // private String email;
}
