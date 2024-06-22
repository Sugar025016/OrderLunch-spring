package com.order_lunch.controller;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.servlet.http.HttpSession;

import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
// import com.order_lunch.util.SecurityUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.jayway.jsonpath.JsonPath;
import com.order_lunch.repository.IUserRepository;

@SpringBootTest
@AutoConfigureMockMvc // 自动配置 MockMvc
@ActiveProfiles("test") // 加載 application-test.properties 文件配置
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserControllerTest {
        @Autowired
        private MockMvc mockMvc;

        private HttpSession session;

        private String captchaText;

        @Autowired
        private IUserRepository iUserRepository;

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
        void testRegister() throws Exception {

                JSONObject validUserRequest = new JSONObject();
                validUserRequest.put("name", "John");
                validUserRequest.put("account", "john123@gmail.com");
                validUserRequest.put("password", "password");
                validUserRequest.put("passwordCheck", "password");
                validUserRequest.put("verifyCode", captchaText);

                mockMvc.perform(post("/user/register")
                                .session((MockHttpSession) session)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(validUserRequest.toString().getBytes())
                                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                                .andExpect(status().isOk())
                                .andReturn();
        }

        @Test
        void testRegisterDataError() throws Exception {
                String userRequest = new JSONObject()
                                .put("name", "")
                                .put("account", "")
                                .put("password", "")
                                .put("passwordCheck", "")
                                .put("captcha", "")
                                .toString();
                MvcResult validRequestResult = mockMvc.perform(MockMvcRequestBuilders.post("/user/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(userRequest)
                                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                                .andExpect(status().isBadRequest())
                                .andExpect(result -> {
                                        String nameError = JsonPath.read(result.getResponse().getContentAsString(),
                                                        "$.name");
                                        assertTrue("must not be blank".equals(nameError)
                                                        || "size must be between 3 and 32".equals(nameError));
                                })
                                .andExpect(MockMvcResultMatchers.jsonPath("$.account").value(Matchers.anyOf(
                                                Matchers.is("must be a well-formed email address"),
                                                Matchers.is("must not be blank"),
                                                Matchers.is("size must be between 4 and 64"))))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.password").value(Matchers.anyOf(
                                                Matchers.is("must not be blank"),
                                                Matchers.is("size must be between 8 and 16"))))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.passwordCheck").value(Matchers.anyOf(
                                                Matchers.is("must not be blank"),
                                                Matchers.is("size must be between 8 and 16"))))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.verifyCode").value(Matchers.anyOf(
                                                Matchers.is("must not be blank"),
                                                Matchers.is("size must be between 4 and 4"))))
                                .andReturn();

                String validResponseContent = validRequestResult.getResponse().getContentAsString();
                // System.out.println("///////////////////////////: " + validRequestResult);
                // System.out.println("///////////////////////////: " + validResponseContent);

        }

        @Test
        void testRegisterCaptchaError() throws Exception {

                JSONObject validUserRequest = new JSONObject();
                validUserRequest.put("name", "John");
                validUserRequest.put("account", "john123@gmail.com");
                validUserRequest.put("password", "password");
                validUserRequest.put("passwordCheck", "password");
                validUserRequest.put("captcha", "");

                mockMvc.perform(post("/user/register")
                                .session((MockHttpSession) session)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(validUserRequest.toString().getBytes())
                                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                                .andExpect(status().isBadRequest())
                                .andReturn();
        }

        @Test
        void testGetUser() throws Exception {

                mockMvc.perform(MockMvcRequestBuilders.post("/login")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED) // 设置 Content-Type 头部
                                .session((MockHttpSession) session)
                                .param("username", "user@testgmail.com")
                                .param("password", "password")
                                .param("captcha", captchaText)
                                .param("rememberMe", "true"))// 添加 CSRF 令牌
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andReturn();

                MvcResult validRequestResult = mockMvc.perform(MockMvcRequestBuilders.get("/user")
                                .session((MockHttpSession) session))
                                .andExpect(jsonPath("$.account").value("user@testgmail.com"))
                                .andExpect(jsonPath("$.name").value("user"))
                                .andExpect(jsonPath("$.email").isEmpty())
                                .andExpect(jsonPath("$.phone").value("123456789"))
                                .andExpect(jsonPath("$.favoriteShops").isEmpty())
                                .andExpect(jsonPath("$.cartShopId").value(0))
                                .andExpect(jsonPath("$.cartCount").value(0))
                                .andExpect(jsonPath("$.cartLat").isEmpty())
                                .andExpect(jsonPath("$.cartLng").isEmpty())
                                .andExpect(jsonPath("$.cartDeliveryKm").isEmpty())
                                .andExpect(jsonPath("$.orderCount").value(0))
                                .andExpect(jsonPath("$.shopOrderCount").value(0))
                                .andExpect(jsonPath("$.address").isEmpty())

                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andReturn();
                String validResponseContent = validRequestResult.getResponse().getContentAsString();
                System.out.println("///////////////////////////: " + validRequestResult);
                System.out.println("///////////////////////////: " + validResponseContent);
        }

        @Test
        void testGetGoogle() {

        }

        @Test
        void testGetLoves() {

        }

        @Test
        void testPutUser() {

        }

        @Test
        void testPutUserPassword() {

        }
}
