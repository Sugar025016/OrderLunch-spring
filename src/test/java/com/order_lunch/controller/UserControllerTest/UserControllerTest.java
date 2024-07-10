package com.order_lunch.controller.UserControllerTest;

import static org.hamcrest.Matchers.hasSize;
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
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc // 自動配置 MockMvc
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
                String mvcResultContent = mvcResult.getResponse().getContentAsString();
                System.out.println("mvcResult: " + mvcResult);
                System.out.println("mvcResultContent: " + mvcResultContent);
                session = mvcResult.getRequest().getSession();
                captchaText = (String) session.getAttribute("captchaText");
                MvcResult validRequestResult = mockMvc.perform(MockMvcRequestBuilders.post("/login")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED) // 设置 Content-Type 头部
                                .session((MockHttpSession) session)
                                .param("username", "user@testgmail.com")
                                .param("password", "password")
                                .param("captcha", captchaText)
                                .param("rememberMe", "true"))
                                .andExpect(status().isOk())
                                .andReturn();
                String validResponseContent = validRequestResult.getResponse().getContentAsString();
                System.out.println("mvcResult: " + mvcResult);
                System.out.println("validRequestResult: " + validRequestResult);
                System.out.println("validResponseContent: " + validResponseContent);

        }

        @Test
        @Rollback
        @Transactional
        void testGetUser_Success() throws Exception {

                MvcResult validRequestResult = mockMvc.perform(MockMvcRequestBuilders.get("/user")
                                .session((MockHttpSession) session))
                                .andExpect(jsonPath("$.account").value("user@testgmail.com"))
                                .andExpect(jsonPath("$.name").value("user"))
                                .andExpect(jsonPath("$.email").isEmpty())
                                .andExpect(jsonPath("$.phone").value("123456789"))
                                .andExpect(jsonPath("$.favoriteShops", hasSize(2)))
                                .andExpect(jsonPath("$.favoriteShops[0].name").value("valueA"))
                                .andExpect(jsonPath("$.favoriteShops[1].name").value("valueB"))
                                .andExpect(jsonPath("$.cartShopId").value(0))
                                .andExpect(jsonPath("$.cartCount").value(0))
                                .andExpect(jsonPath("$.cartLat").isEmpty())
                                .andExpect(jsonPath("$.cartLng").isEmpty())
                                .andExpect(jsonPath("$.cartDeliveryKm").isEmpty())
                                .andExpect(jsonPath("$.orderCount").value(0))
                                .andExpect(jsonPath("$.shopOrderCount").value(0))
                                .andExpect(jsonPath("$.address").isEmpty())
                                .andExpect(status().isOk())
                                .andReturn();
                String validResponseContent = validRequestResult.getResponse().getContentAsString();
                System.out.println("validRequestResult: " + validRequestResult);
                System.out.println("validResponseContent: " + validResponseContent);
        }

        @Test
        @Rollback
        @Transactional
        void testPutUser_Success() throws Exception {

                String userRequest = new JSONObject()
                                .put("name", "John Doe")
                                .put("phone", "9876543211")
                                .put("email", "johndoe@example.com")
                                .toString();

                MvcResult validRequestResult1 = mockMvc.perform(MockMvcRequestBuilders.put("/user")
                                .session((MockHttpSession) session)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(userRequest)
                                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                                .andExpect(status().isOk())
                                .andReturn();
                String validResponseContent1 = validRequestResult1.getResponse().getContentAsString();
                System.out.println("validRequestResult1: " + validRequestResult1);
                System.out.println("validResponseContent1: " + validResponseContent1);

                MvcResult validRequestResult = mockMvc.perform(MockMvcRequestBuilders.get("/user")
                                .session((MockHttpSession) session))
                                .andExpect(jsonPath("$.account").value("user@testgmail.com"))
                                .andExpect(jsonPath("$.name").value("John Doe"))
                                .andExpect(jsonPath("$.email").isEmpty())
                                .andExpect(jsonPath("$.phone").value("9876543211"))
                                .andExpect(jsonPath("$.favoriteShops", hasSize(2)))
                                .andExpect(jsonPath("$.favoriteShops[0].name").value("valueA"))
                                .andExpect(jsonPath("$.favoriteShops[1].name").value("valueB"))
                                .andExpect(jsonPath("$.cartShopId").value(0))
                                .andExpect(jsonPath("$.cartCount").value(0))
                                .andExpect(jsonPath("$.cartLat").isEmpty())
                                .andExpect(jsonPath("$.cartLng").isEmpty())
                                .andExpect(jsonPath("$.cartDeliveryKm").isEmpty())
                                .andExpect(jsonPath("$.orderCount").value(0))
                                .andExpect(jsonPath("$.shopOrderCount").value(0))
                                .andExpect(jsonPath("$.address").isEmpty())
                                .andExpect(status().isOk())
                                .andReturn();
                String validResponseContent = validRequestResult.getResponse().getContentAsString();
                System.out.println("validRequestResult: " + validRequestResult);
                System.out.println("validResponseContent: " + validResponseContent);
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
                                                Matchers.is("must be a well-formed email address"),
                                                Matchers.is("size must be between 8 and 225"))));

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
        @Rollback
        @Transactional
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
                                                Matchers.is("size must be between 8 and 16"))));

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
                                .andReturn();

                String validResponseContent = validRequestResult.getResponse().getContentAsString();
                System.out.println("validRequestResult: " + validRequestResult);
                System.out.println("validResponseContent: " + validResponseContent);
        }

        @Test
        void testGetLoves_Success() throws Exception {

                MvcResult validRequestResult = mockMvc.perform(get("/user/favorite")
                                .session((MockHttpSession) session)
                                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                                .andExpect(status().isOk())
                                .andExpect(content().contentType("application/json"))
                                .andExpect(jsonPath("$[0].id").value(1))
                                .andExpect(jsonPath("$[0].name").value("valueA"))
                                .andExpect(jsonPath("$[0].description").value("description55555555555666"))
                                .andExpect(jsonPath("$[0].imgUrl").value("http://localhost:8082/17015922948981374.jpg"))
                                .andExpect(jsonPath("$[0].orderable").value(true))
                                .andExpect(jsonPath("$[1].id").value(2))
                                .andExpect(jsonPath("$[1].name").value("valueB"))
                                .andExpect(jsonPath("$[1].imgUrl").value("http://localhost:8082/1691994708919221.jpg"))
                                .andExpect(jsonPath("$[1].orderable").value(true))
                                .andReturn();

                String validResponseContent = validRequestResult.getResponse().getContentAsString();
                System.out.println("validRequestResult: " + validRequestResult);
                System.out.println("validResponseContent: " + validResponseContent);
        }

        @Test
        @Rollback
        @Transactional
        void testAddOrDeleteUserLove_DeleteSuccess() throws Exception {
                mockMvc.perform(put("/user/favorite/2")
                                .session((MockHttpSession) session)
                                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                                .andExpect(status().isOk())
                                .andReturn();

                MvcResult validRequestResult = mockMvc.perform(get("/user/favorite")
                                .session((MockHttpSession) session)
                                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                                .andExpect(status().isOk())
                                .andExpect(content().contentType("application/json"))
                                .andExpect(jsonPath("$[0].id").value(1))
                                .andExpect(jsonPath("$[0].name").value("valueA"))
                                .andExpect(jsonPath("$[0].description").value("description55555555555666"))
                                .andExpect(jsonPath("$[0].imgUrl").value("http://localhost:8082/17015922948981374.jpg"))
                                .andExpect(jsonPath("$[0].orderable").value(true))
                                .andReturn();
                String validResponseContent = validRequestResult.getResponse().getContentAsString();
                System.out.println("validRequestResult: " + validRequestResult);
                System.out.println("validResponseContent: " + validResponseContent);

        }

        @Test
        @Rollback
        @Transactional
        void testAddOrDeleteUserLove_AddSuccess() throws Exception {
                mockMvc.perform(put("/user/favorite/5")
                                .session((MockHttpSession) session)
                                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                                .andExpect(status().isOk())
                                .andReturn();

                MvcResult validRequestResult = mockMvc.perform(get("/user/favorite")
                                .session((MockHttpSession) session)
                                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                                .andExpect(status().isOk())
                                .andExpect(content().contentType("application/json"))
                                .andExpect(jsonPath("$[0].id").value(1))
                                .andExpect(jsonPath("$[0].name").value("valueA"))
                                .andExpect(jsonPath("$[0].description").value("description55555555555666"))
                                .andExpect(jsonPath("$[0].imgUrl").value("http://localhost:8082/17015922948981374.jpg"))
                                .andExpect(jsonPath("$[0].orderable").value(true))
                                .andExpect(jsonPath("$[1].id").value(2))
                                .andExpect(jsonPath("$[1].name").value("valueB"))
                                .andExpect(jsonPath("$[1].imgUrl").value("http://localhost:8082/1691994708919221.jpg"))
                                .andExpect(jsonPath("$[1].orderable").value(true))
                                .andExpect(jsonPath("$[2].id").value(5))
                                .andExpect(jsonPath("$[2].name").value("newNAME"))
                                .andExpect(jsonPath("$[2].description").value("description"))
                                .andExpect(jsonPath("$[2].imgUrl").value("http://localhost:8082/1691922420438552.jpg"))
                                .andExpect(jsonPath("$[2].orderable").value(false))
                                .andReturn();

                String validResponseContent = validRequestResult.getResponse().getContentAsString();
                System.out.println("validRequestResult: " + validRequestResult);
                System.out.println("validResponseContent: " + validResponseContent);

        }

        @Test
        @Rollback
        @Transactional
        void testAddOrDeleteUserLove_Mismatch() throws Exception {
                MvcResult validRequestResult = mockMvc.perform(put("/user/favorite/999")
                                .session((MockHttpSession) session)
                                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.code").value(611))
                                .andExpect(jsonPath("$.message").value("商店不存在"))
                                .andReturn();

                String validResponseContent = validRequestResult.getResponse().getContentAsString();
                System.out.println("validRequestResult: " + validRequestResult);
                System.out.println("validResponseContent: " + validResponseContent);

        }

        @Test
        @Rollback
        @Transactional
        void putDeliveryAddress_Success() throws Exception {
                mockMvc.perform(put("/user/addressDelivery/40")
                                .session((MockHttpSession) session)
                                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                                .andExpect(status().isOk())
                                .andReturn();

                                MvcResult validRequestResult = mockMvc.perform(MockMvcRequestBuilders.get("/user")
                                .session((MockHttpSession) session))
                                .andExpect(jsonPath("$.address.id").value(40))
                                .andExpect(jsonPath("$.address.city").value("台南市"))
                                .andExpect(jsonPath("$.address.area").value("南區"))
                                .andExpect(jsonPath("$.address.street").value("大同路２段"))
                                .andExpect(jsonPath("$.address.detail").value("123號"))
                                .andExpect(jsonPath("$.address.lat").value(22.9779755))
                                .andExpect(jsonPath("$.address.lng").value(120.2136988))
                                .andExpect(status().isOk())
                                .andReturn();

                String validResponseContent = validRequestResult.getResponse().getContentAsString();
                System.out.println("validRequestResult: " + validRequestResult);
                System.out.println("validResponseContent: " + validResponseContent);

        }


}
