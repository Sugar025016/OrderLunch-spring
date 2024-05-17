package com.order_lunch.config.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.order_lunch.enums.NewErrorStatus;

public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {


        // responseData.put("ok", false);
        if (exception instanceof BadCredentialsException) {
            // 用戶名或密碼錯誤

            response.setStatus(NewErrorStatus.CAPTCHA_MISTAKE.getKey());
        } else if (exception instanceof AuthenticationServiceException) {
            // 用戶名或密碼錯誤

            response.setStatus(NewErrorStatus.ACCOUNT_OR_PASSWORD_MISTAKE.getKey());

        } else if (exception instanceof LockedException) {
            // 帳戶被鎖定

            response.setStatus(NewErrorStatus.CAPTCHA_MISTAKE.getKey());
        } else if (exception instanceof DisabledException) {
            // 帳戶被禁用

            response.setStatus(NewErrorStatus.CAPTCHA_MISTAKE.getKey());
        } else {
            // 其他驗證失敗

            response.setStatus(NewErrorStatus.CAPTCHA_MISTAKE.getKey());
        }

        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        // response.getWriter().write(new  ObjectMapper().writeValueAsString(errorResponse));
    }

}
