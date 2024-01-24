package com.order_lunch.config;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.authentication.WebAuthenticationDetails;

public class MyWebAuthenticationDetails extends WebAuthenticationDetails {

    private String imageCode;

    public String getImageCode() {
        return imageCode;
    }

    /**
     * 补充用户提交的验证码和session保存的验证码
     */  
    public MyWebAuthenticationDetails(HttpServletRequest request) {
        super(request);
        this.imageCode = request.getParameter("captcha");

    }

}