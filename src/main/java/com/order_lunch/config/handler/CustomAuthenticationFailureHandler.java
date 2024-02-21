package com.order_lunch.config.handler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.order_lunch.enums.NewErrorStatus;
import com.order_lunch.model.ErrorResponse;

public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {

        Map<String, Object> responseData = new HashMap<>();

        ErrorResponse errorResponse = new ErrorResponse();

        // responseData.put("ok", false);
        if (exception instanceof BadCredentialsException) {
            // 用戶名或密碼錯誤
            // responseData.put("message", "驗證碼錯誤");
            // responseData.put("code", 401);
            // responseData.put("message", "用戶名或密碼錯誤");
            // responseData.put("code", 401);
            errorResponse.setCode(NewErrorStatus.CAPTCHA_MISTAKE.getKey());
            errorResponse.setMessage(exception.getMessage());
        } else if (exception instanceof AuthenticationServiceException) {
            // 用戶名或密碼錯誤

            errorResponse.setCode(NewErrorStatus.ACCOUNT_OR_PASSWORD_MISTAKE.getKey());
            errorResponse.setMessage(exception.getMessage());
            // responseData.put("message", "用戶名或密碼錯誤");
            // responseData.put("code", 401);
            // response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); 
            
        } else if (exception instanceof LockedException) {
            // 帳戶被鎖定

            errorResponse.setCode(NewErrorStatus.CAPTCHA_MISTAKE.getKey());
            errorResponse.setMessage(exception.getMessage());
            // response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); 
        } else if (exception instanceof DisabledException) {
            // 帳戶被禁用

            errorResponse.setCode(NewErrorStatus.CAPTCHA_MISTAKE.getKey());
            errorResponse.setMessage(exception.getMessage());
            // response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); 
        } else {
            // 其他驗證失敗

            errorResponse.setCode(NewErrorStatus.CAPTCHA_MISTAKE.getKey());
            errorResponse.setMessage(exception.getMessage());
            // response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); 
        }
        // responseData.put("message", "登錄失敗");
        // responseData.put("code", 401);
        // 將 JSON 寫入 response 中
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(new ObjectMapper().writeValueAsString(errorResponse));
    }

}


