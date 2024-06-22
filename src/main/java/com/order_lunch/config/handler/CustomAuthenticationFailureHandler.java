package com.order_lunch.config.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
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

        ErrorResponse errorResponse = new ErrorResponse();
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        
        if (exception instanceof BadCredentialsException) {
            // 錯誤憑證異常

            // String errorMessage = exception.getMessage();
            // errorResponse.setMessage(errorMessage);
            errorResponse.setCode(NewErrorStatus.CAPTCHA_ERROR.getKey());
            errorResponse.setMessage(NewErrorStatus.CAPTCHA_ERROR.getChinese());

        } else if (exception instanceof AuthenticationServiceException) {
            // 用戶名或密碼錯誤
            errorResponse.setCode(NewErrorStatus.ACCOUNT_OR_PASSWORD_MISTAKE.getKey());
            errorResponse.setMessage(NewErrorStatus.ACCOUNT_OR_PASSWORD_MISTAKE.getChinese());

        } else if (exception instanceof LockedException) {
            // 帳戶被鎖定 //懶的寫了....

            response.setStatus(NewErrorStatus.CAPTCHA_MISTAKE.getKey());
        } else if (exception instanceof DisabledException) {
            // 帳戶被禁用//懶的寫了....

            response.setStatus(NewErrorStatus.CAPTCHA_MISTAKE.getKey());
        } else {
            // 其他驗證失敗//懶的寫了....

            response.setStatus(NewErrorStatus.CAPTCHA_MISTAKE.getKey());
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        ObjectMapper mapper = new ObjectMapper();
        String jsonResponse = mapper.writeValueAsString(errorResponse);
        response.getWriter().write(jsonResponse);
    }

}
