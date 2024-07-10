package com.order_lunch.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test") // 加載 application-test.properties 文件配置
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SecurityLoginTest {

    @Autowired
    private MockMvc mockMvc;

    private HttpSession session;

    private String captchaText;

    @BeforeEach
    void setUp() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/register/captcha")
                .param("timestamp", "1234567890"))
                .andExpect(status().isOk())
                .andReturn();

        session = mvcResult.getRequest().getSession();
        captchaText = (String) session.getAttribute("captchaText");
    }

    @Test
    void testLoginWithValidCredentials() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED) 
                .session((MockHttpSession) session)
                .param("username", "user@testgmail.com")
                .param("password", "password")
                .param("captcha", captchaText)
                .param("rememberMe", "true"))
                .andExpect(status().isOk())
                .andReturn();
        System.out.println("result:" + result);

    }

    @Test
    void testLoginWithInvalidCredentials() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/login")
                .param("username", "user@testgmail.com")
                .param("password", "password")
                .param("captcha", captchaText)
                .param("rememberMe", "true"))
                .andReturn();
        System.out.println("result:" + result);
    }

    @Test
    void testLoginWithAccountError() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/login")
                .session((MockHttpSession) session)
                .param("username", "user@tmal.com")
                .param("password", "password")
                .param("captcha", captchaText)
                .param("rememberMe", "true"))
                .andExpect(jsonPath("$.code").value("401"))
                .andExpect(jsonPath("$.message").value("帳號或密碼錯誤"))
                .andExpect(jsonPath("$.ok").value("false"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();
        System.out.println("result:" + result);
    }

    @Test
    void testLoginWithPasswordError() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/login")
                .session((MockHttpSession) session)
                .param("username", "user@testgmail.com")
                .param("password", "pard")
                .param("captcha", captchaText)
                .param("rememberMe", "true"))
                .andExpect(jsonPath("$.code").value("401"))
                .andExpect(jsonPath("$.message").value("帳號或密碼錯誤"))
                .andExpect(jsonPath("$.ok").value("false"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();
        System.out.println("result:" + result);
    }

    @Test
    void testLoginWithCaptchaNull() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/login")
                .param("username", "user@testgmail.com")
                .param("password", "password")
                .param("captcha", "")
                .param("rememberMe", "true"))
                .andExpect(jsonPath("$.code").value("411"))
                .andExpect(jsonPath("$.message").value("驗證碼錯誤"))
                .andExpect(jsonPath("$.ok").value("false"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();
        System.out.println("result:" + result);
    }

    @Test
    void testLoginWithCaptchaError() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/login")
                .param("username", "user@testgmail.com")
                .param("password", "password")
                .param("captcha", "1234")
                .param("rememberMe", "true"))
                .andExpect(jsonPath("$.code").value("411"))
                .andExpect(jsonPath("$.message").value("驗證碼錯誤"))
                .andExpect(jsonPath("$.ok").value("false"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();
        System.out.println("result:" + result);
    }
}
