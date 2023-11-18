package com.order_lunch.model.response;

import java.time.LocalDateTime;

import org.springframework.beans.BeanUtils;

import com.order_lunch.entity.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class BackstageUserResponse {

    private int cartCount;

    private int id;

    private String account;

    private String password;

    private String name;

    private String phone;

    @JsonProperty("roleName")
    private String role;

    private String email;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    public BackstageUserResponse(User user) {
        BeanUtils.copyProperties(user,this);
        // this.nickname = user.getNickname();
        // this.account=user.getAccount();

    }

}
