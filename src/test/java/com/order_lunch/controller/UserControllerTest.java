package com.order_lunch.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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

@SpringBootTest
@AutoConfigureMockMvc // 自动配置 MockMvc
@ActiveProfiles("test") // 加載 application-test.properties 文件配置
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserControllerTest {
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
                mockMvc.perform(MockMvcRequestBuilders.post("/login")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED) // 设置 Content-Type 头部
                                .session((MockHttpSession) session)
                                .param("username", "user@testgmail.com")
                                .param("password", "password")
                                .param("captcha", captchaText)
                                .param("rememberMe", "true"))
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andReturn();

        }

        @Test
        void testGetUser_Success() throws Exception {

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
        void testPutUser_Success() throws Exception {

                String userRequest = new JSONObject()
                                .put("name", "John Doe")
                                .put("phone", "9876543211")
                                .put("email", "johndoe@example.com")
                                .toString();

                mockMvc.perform(MockMvcRequestBuilders.put("/user")
                                .session((MockHttpSession) session)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(userRequest)
                                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                                .andExpect(status().isOk());

                MvcResult validRequestResult = mockMvc.perform(MockMvcRequestBuilders.get("/user")
                                .session((MockHttpSession) session))
                                .andExpect(jsonPath("$.account").value("user@testgmail.com"))
                                .andExpect(jsonPath("$.name").value("John Doe"))
                                .andExpect(jsonPath("$.email").isEmpty())
                                .andExpect(jsonPath("$.phone").value("9876543211"))
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
        }

        @Test
        void testPutUser_valid() throws Exception {

                String userRequest = new JSONObject()
                                .put("name", "")
                                .put("phone", "123")
                                .put("email", "johndoe")
                                .toString();

                mockMvc.perform(put("/user")
                                .session((MockHttpSession) session)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(userRequest)
                                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                                .andExpect(status().isBadRequest())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(Matchers.anyOf(
                                                Matchers.is("must not be blank"),
                                                Matchers.is("size must be between 3 and 32"))))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.phone").value(Matchers.anyOf(
                                                Matchers.is("must not be blank"),
                                                Matchers.is("size must be between 10 and 11"))))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(Matchers.anyOf(
                                                Matchers.is("must not be blank"),
                                                Matchers.is("size must be between 8 and 225")))); // 假设返回的 JSON

        }


        // 未必要在這邊驗證
        @Test
        void testPutUser_Unauthorized() throws Exception {

                mockMvc.perform(get("/logout")
                                .session((MockHttpSession) session))
                                .andExpect(status().isOk());

                String userRequest = new JSONObject()
                                .put("name", "John Doe")
                                .put("phone", "9876543211")
                                .put("email", "johndoe@example.com")
                                .toString();

                mockMvc.perform(put("/user")
                                .session((MockHttpSession) session)
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(userRequest))
                                .andExpect(status().isUnauthorized());

        }

        @Test
        void testPutUserPassword_Success() throws Exception {

                String userRequest = new JSONObject()
                                .put("password", "password")
                                .put("newPassword", "qazwsxedc")
                                .toString();

                mockMvc.perform(put("/user/pwd")
                                .session((MockHttpSession) session)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(userRequest)
                                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                                .andExpect(status().isOk())
                                .andReturn();

        }

        @Test
        void testPutUserPassword_valid() throws Exception {

                String userRequest = new JSONObject()
                                .put("password", "")
                                .put("newPassword", "123")
                                .toString();

                mockMvc.perform(put("/user/pwd")
                                .session((MockHttpSession) session)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(userRequest)
                                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                                .andExpect(status().isBadRequest())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.password").value(Matchers.anyOf(
                                                Matchers.is("must not be blank"),
                                                Matchers.is("size must be between 8 and 16"))))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.newPassword").value(Matchers.anyOf(
                                                Matchers.is("must not be blank"),
                                                Matchers.is("size must be between 8 and 16")))); // 假设返回的 JSON

        }

        @Test
        void testPutUserPassword_PasswordMismatch() throws Exception {

                String userRequest = new JSONObject()
                                .put("password", "Mismatch")
                                .put("newPassword", "123456789")
                                .toString();

                MvcResult validRequestResult = mockMvc.perform(put("/user/pwd")
                                .session((MockHttpSession) session)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(userRequest)
                                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.code").value(402))
                                .andReturn(); // 假设返回的 JSON

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

}
