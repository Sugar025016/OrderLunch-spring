package com.order_lunch.model.request;

import javax.validation.constraints.Email;
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
public class UserPutRequest {

    @NotNull(message = "name 不能為空")
    @Size(min=3,max=16)
    private String name;

    @NotNull(message = "phone 不能為空")
    @Size(min=10,max=11)
    private String phone;

    @Size(min=8,max=225)
    @Email
    private String email;

}
