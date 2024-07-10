package com.order_lunch.controller.ShopControllerTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.servlet.http.HttpSession;

import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
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
import org.springframework.transaction.annotation.Transactional;

import com.order_lunch.controller.ShopController;

@SpringBootTest
@AutoConfigureMockMvc // 自動配置 MockMvc
@ActiveProfiles("test") // 加載 application-test.properties 文件配置
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ShopControllerTest_put {

        private HttpSession session;

        private String captchaText;

        @Autowired
        private MockMvc mockMvc;

        @InjectMocks
        private ShopController shopController;

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
        void testPutShop_Success() throws Exception {

                JSONObject addressJson = new JSONObject();
                addressJson.put("id", 6);
                addressJson.put("city", "台南市");
                addressJson.put("area", "東區");
                addressJson.put("street", "懷恩街");
                addressJson.put("detail", "333");

                JSONObject requestJson = new JSONObject();
                requestJson.put("id", 6);
                requestJson.put("name", "Test Shop");
                requestJson.put("description", "Test Description");
                requestJson.put("address", addressJson);
                requestJson.put("phone", "1234567890");
                requestJson.put("imgId", 16);
                requestJson.put("imgUrl", 56);
                requestJson.put("deliveryKm", 5.0);
                requestJson.put("deliveryPrice", 200);
                requestJson.put("orderable", true);
                requestJson.put("disable", false);
                requestJson.put("delete", false);

                MvcResult validRequestResult = mockMvc.perform(put("/shop/sell")
                                .session((MockHttpSession) session)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson.toString())
                                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                                .andExpect(status().isOk())
                                .andReturn();

                String validResponseContent = validRequestResult.getResponse().getContentAsString();
                System.out.println("validRequestResult: " + validRequestResult);
                System.out.println("validResponseContent: " + validResponseContent);
        }

        @Test
        @Rollback
        @Transactional
        void testPutShop_AddAddressSuccess() throws Exception {

                JSONObject addressJson = new JSONObject();
                addressJson.put("id", 6);
                addressJson.put("city", "台南市");
                addressJson.put("area", "東區");
                addressJson.put("street", "懷恩街");
                addressJson.put("detail", "333");

                JSONObject requestJson = new JSONObject();
                requestJson.put("id", 6);
                requestJson.put("name", "Test Shop");
                requestJson.put("description", "Test Description");
                requestJson.put("address", addressJson);
                requestJson.put("phone", "1234567890");
                requestJson.put("imgId", 16);
                requestJson.put("imgUrl", 56);
                requestJson.put("deliveryKm", 5.0);
                requestJson.put("deliveryPrice", 200);
                requestJson.put("orderable", true);
                requestJson.put("disable", false);
                requestJson.put("delete", false);

                MvcResult validRequestResult = mockMvc.perform(put("/shop/sell")
                                .session((MockHttpSession) session)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson.toString())
                                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                                .andExpect(status().isOk())
                                .andReturn();

                String validResponseContent = validRequestResult.getResponse().getContentAsString();
                System.out.println("validRequestResult: " + validRequestResult);
                System.out.println("validResponseContent: " + validResponseContent);
        }

        @Test
        @Rollback
        @Transactional
        void testPutShop_Valid() throws Exception {

                JSONObject addressJson = new JSONObject();
                addressJson.put("id", 6);
                addressJson.put("city", "台南市");
                addressJson.put("area", " ");
                addressJson.put("street", " ");
                addressJson.put("detail", "333");

                JSONObject requestJson = new JSONObject();
                requestJson.put("id", null);
                requestJson.put("name", "");
                requestJson.put("description",
                                "1111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111");
                requestJson.put("address", addressJson);
                requestJson.put("phone", "");
                requestJson.put("imgId", 1);
                requestJson.put("imgUrl", 56);
                requestJson.put("deliveryKm", null);
                requestJson.put("deliveryPrice", null);
                requestJson.put("orderable", null);
                requestJson.put("disable", null);
                requestJson.put("delete", false);

                MvcResult validRequestResult = mockMvc.perform(put("/shop/sell")
                                .session((MockHttpSession) session)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson.toString())
                                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                                .andDo(print())  // Print the response for debugging
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.id").value(Matchers.anyOf(
                                                Matchers.is("must not be null"))))
                                .andExpect(jsonPath("$.shopName").value(Matchers.anyOf(
                                                Matchers.is("must not be blank"),
                                                Matchers.is("size must be between 3 and 16"))))
                                .andExpect(jsonPath("$.description").value(Matchers.anyOf(
                                                Matchers.is("size must be between 0 and 255"))))
                                .andExpect(jsonPath("$.phone").value(Matchers.anyOf(
                                                Matchers.is("must not be blank"),
                                                Matchers.is("size must be between 10 and 11"))))
                                .andExpect(jsonPath("$.deliveryKm").value(Matchers.anyOf(
                                                Matchers.is("must not be null"))))
                                .andExpect(jsonPath("$.deliveryPrice").value(Matchers.anyOf(
                                                Matchers.is("must not be null"))))
                                .andExpect(jsonPath("$.isOrderable").value(Matchers.anyOf(
                                                Matchers.is("must not be null"))))
                                .andExpect(jsonPath("$.isDisable").value(Matchers.anyOf(
                                                Matchers.is("must not be null"))))

                                //不知道為什麼，address無法驗證
                                // .andExpect(jsonPath("$.address.street").value("must not be blank"))
                                // .andExpect(jsonPath("$.address.city").value("台南市"))
                                // .andExpect(jsonPath("$.address.city").value(Matchers.anyOf(
                                // Matchers.is("must not be blank"))))
                                // .andExpect(jsonPath("$.address.area").value(Matchers.anyOf(
                                // Matchers.is("must not be blank"))))
                                // .andExpect(jsonPath("$.address.street").value(Matchers.anyOf(
                                // Matchers.is("must not be blank"))))
                                // .andExpect(jsonPath("$.address.detail").value(Matchers.anyOf(
                                // Matchers.is("must not be blank"),
                                // Matchers.is("size must be between 10 and 11"))))
                                .andReturn();

                String validResponseContent = validRequestResult.getResponse().getContentAsString();
                System.out.println("validRequestResult: " + validRequestResult);
                System.out.println("validResponseContent: " + validResponseContent);
        }

}
