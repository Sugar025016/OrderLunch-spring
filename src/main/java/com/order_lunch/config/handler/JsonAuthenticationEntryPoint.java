package com.order_lunch.config.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.order_lunch.enums.NewErrorStatus;
import com.order_lunch.model.ErrorResponse;


//判定位登入拋出異常
@Component
public class JsonAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {

        ErrorResponse errorResponse = new ErrorResponse();
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        errorResponse.setCode(NewErrorStatus.NOT_LOGIN.getKey());
        errorResponse.setMessage(NewErrorStatus.NOT_LOGIN.getChinese());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        ObjectMapper mapper = new ObjectMapper();
        String jsonResponse = mapper.writeValueAsString(errorResponse);
        response.getWriter().write(jsonResponse);


    }
}
