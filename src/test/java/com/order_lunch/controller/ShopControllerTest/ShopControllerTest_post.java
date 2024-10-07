package com.order_lunch.controller.ShopControllerTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.servlet.http.HttpSession;

import org.hamcrest.Matchers;
import org.json.JSONArray;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import com.order_lunch.controller.ShopController;
import com.order_lunch.enums.NewErrorStatus;

@SpringBootTest
@AutoConfigureMockMvc // 自動配置 MockMvc
@ActiveProfiles("test") // 加載 application-test.properties 文件配置
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ShopControllerTest_post {

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
        public void testAddShop_Success() throws Exception {
                MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/register/captcha")
                                .session((MockHttpSession) session)
                                .param("timestamp", "1234567891"))
                                .andExpect(status().isOk())
                                .andReturn();

                session = mvcResult.getRequest().getSession();
                captchaText = (String) session.getAttribute("captchaText");

                JSONObject requestBody = new JSONObject();
                requestBody.put("shopName", "Test Shop");
                requestBody.put("phone", "1234567890");
                requestBody.put("description", "Test Description");
                requestBody.put("addressId", 1);
                requestBody.put("addressDetail", "888888");
                requestBody.put("captcha", captchaText);

                mockMvc.perform(post("/shop/register")
                                .session((MockHttpSession) session)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody.toString())
                                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                                .andExpect(status().isOk())
                                .andReturn();

                MvcResult validRequestResultGetId = mockMvc.perform(get("/shop/sell")
                                .session((MockHttpSession) session))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$[4].name").value("Test Shop"))
                                .andReturn();
                String validResponseContentGetId = validRequestResultGetId.getResponse().getContentAsString();
                System.out.println("validResponseContentGetId: " + validResponseContentGetId);
                System.out.println("validResponseContentGetId: " + validResponseContentGetId);
                JSONArray jsonArray = new JSONArray(validResponseContentGetId);

                int id = 0;
                // 遍历JSONArray对象
                for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        // 检查name是否等于"Test Shop"
                        if ("Test Shop".equals(jsonObject.getString("name"))) {
                                id = jsonObject.getInt("id");
                                break; // 找到后退出循环
                        }
                }

                MvcResult validRequestResult = mockMvc.perform(get("/shop/sell/" + id)
                                .session((MockHttpSession) session)
                                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(id))
                                .andExpect(jsonPath("$.name").value("Test Shop"))
                                .andExpect(jsonPath("$.deliveryKm").value(0.0))
                                .andExpect(jsonPath("$.deliveryPrice").value(0))
                                .andExpect(jsonPath("$.description").value("Test Description"))
                                .andExpect(jsonPath("$.imgId").value(0))
                                .andExpect(jsonPath("$.imgUrl").isEmpty())
                                .andExpect(jsonPath("$.phone").value("1234567890"))
                                .andExpect(jsonPath("$.schedules[0].week").value(0))
                                .andExpect(jsonPath("$.schedules[1].week").value(1))
                                .andExpect(jsonPath("$.schedules[2].week").value(2))
                                .andExpect(jsonPath("$.schedules[3].week").value(3))
                                .andExpect(jsonPath("$.schedules[4].week").value(4))
                                .andExpect(jsonPath("$.schedules[5].week").value(5))
                                .andExpect(jsonPath("$.schedules[6].week").value(6))
                                .andExpect(jsonPath("$.orderable").value(false))
                                .andExpect(jsonPath("$.open").value(false))
                                .andExpect(jsonPath("$.address.city").value("台北市"))
                                .andExpect(jsonPath("$.address.area").value("中正區"))
                                .andExpect(jsonPath("$.address.street").value("八德路１段"))
                                .andExpect(jsonPath("$.address.detail").value("888888"))
                                .andExpect(jsonPath("$.address.lat").value(25.0421407))
                                .andExpect(jsonPath("$.address.lng").value(121.5198716))
                                .andExpect(jsonPath("$.tabProducts").doesNotExist()) // Assuming tabProducts should be
                                .andExpect(jsonPath("$.products").isArray())
                                .andExpect(jsonPath("$.products.length()").value(0))
                                .andReturn();
                String validResponseContent = validRequestResult.getResponse().getContentAsString();
                System.out.println("validRequestResult: " + validRequestResult);
                System.out.println("validResponseContent: " + validResponseContent);
        }

        @Test
        public void testAddShop_Valid() throws Exception {
                JSONObject requestBody = new JSONObject();
                requestBody.put("shopName", "");
                requestBody.put("phone", "");
                requestBody.put("description", "11111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111");
                requestBody.put("addressId", null);
                requestBody.put("addressDetail", "");
                requestBody.put("captcha", "");

                MvcResult validRequestResult = mockMvc.perform(post("/shop/register")
                                .session((MockHttpSession) session)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody.toString())
                                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                                .andExpect(status().isBadRequest())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(Matchers.anyOf(
                                                Matchers.is("must not be blank"),
                                                Matchers.is("size must be between 3 and 16"))))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.phone").value(Matchers.anyOf(
                                                Matchers.is("must not be blank"),
                                                Matchers.is("size must be between 10 and 11"))))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.addressDetail").value(Matchers.anyOf(
                                                Matchers.is("must not be blank"))))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.captcha").value(Matchers.anyOf(
                                                Matchers.is("must not be blank"),
                                                Matchers.is("size must be between 4 and 4"))))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(Matchers.anyOf(
                                                Matchers.is("size must be between 0 and 255"))))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.addressId").value(Matchers.anyOf(
                                                Matchers.is("must not be null"))))
                                .andReturn();

                String validResponseContent = validRequestResult.getResponse().getContentAsString();
                System.out.println("validRequestResult: " + validRequestResult);
                System.out.println("validResponseContent: " + validResponseContent);
        }

        @Test
        public void testAddShop_AddressIdMismatch() throws Exception {
                MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/register/captcha")
                                .session((MockHttpSession) session)
                                .param("timestamp", "1234567891"))
                                .andExpect(status().isOk())
                                .andReturn();

                session = mvcResult.getRequest().getSession();
                captchaText = (String) session.getAttribute("captchaText");

                JSONObject requestBody = new JSONObject();
                requestBody.put("shopName", "Test Shop");
                requestBody.put("phone", "1234567890");
                requestBody.put("description", "Test Description");
                requestBody.put("addressId", 1000000000);
                requestBody.put("addressDetail", "888888");
                requestBody.put("captcha", captchaText);

                MvcResult validRequestResultGetId = mockMvc.perform(post("/shop/register")
                                .session((MockHttpSession) session)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody.toString())
                                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.code").value(402)) // 验证返回的 JSON 中的 code 字段值为 402
                                .andExpect(jsonPath("$.message").value("AddressData"))
                                .andExpect(jsonPath("$.ok").value(false))
                                .andReturn();

                String validResponseContentGetId = validRequestResultGetId.getResponse().getContentAsString();
                System.out.println("validResponseContentGetId: " + validRequestResultGetId);
                System.out.println("validResponseContentGetId: " + validResponseContentGetId);
        }

        // //換位子
        @Test
        public void testAddShop_NotLogin() throws Exception {
                mockMvc.perform(get("/logout")
                                .session((MockHttpSession) session))
                                .andExpect(status().isOk());

                MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/register/captcha")
                                .session((MockHttpSession) session)
                                .param("timestamp", "1234567891"))
                                .andExpect(status().isOk())
                                .andReturn();

                session = mvcResult.getRequest().getSession();
                captchaText = (String) session.getAttribute("captchaText");

                JSONObject requestBody = new JSONObject();
                requestBody.put("shopName", "Test Shop");
                requestBody.put("phone", "1234567890");
                requestBody.put("description", "Test Description");
                requestBody.put("addressId", 1);
                requestBody.put("addressDetail", "888888");
                requestBody.put("captcha", "1234");

                MvcResult validRequestResultGetId = mockMvc.perform(post("/shop/register")
                                .session((MockHttpSession) session)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody.toString())
                                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                                .andExpect(status().isUnauthorized())
                                .andExpect(jsonPath("$.code").value(NewErrorStatus.NOT_LOGIN.getKey())) // 验证返回的 JSON 中的
                                .andExpect(jsonPath("$.message").value(NewErrorStatus.NOT_LOGIN.getChinese()))
                                .andExpect(jsonPath("$.ok").value(false))
                                .andReturn();

                String validResponseContentGetId = validRequestResultGetId.getResponse().getContentAsString();
                System.out.println("validResponseContentGetId: " + validRequestResultGetId);
                System.out.println("validResponseContentGetId: " + validResponseContentGetId);
        }

        @Test
        public void testAddShop_CaptchaMismatch() throws Exception {
                MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/register/captcha")
                                .session((MockHttpSession) session)
                                .param("timestamp", "1234567891"))
                                .andExpect(status().isOk())
                                .andReturn();

                session = mvcResult.getRequest().getSession();
                captchaText = (String) session.getAttribute("captchaText");

                JSONObject requestBody = new JSONObject();
                requestBody.put("shopName", "Test Shop");
                requestBody.put("phone", "1234567890");
                requestBody.put("description", "Test Description");
                requestBody.put("addressId", 1);
                requestBody.put("addressDetail", "888888");
                requestBody.put("captcha", "1234");

                MvcResult validRequestResultGetId = mockMvc.perform(post("/shop/register")
                                .session((MockHttpSession) session)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody.toString())
                                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.code").value(411)) // 验证返回的 JSON 中的 code 字段值为 402
                                .andExpect(jsonPath("$.message").value("驗證碼錯誤"))
                                .andExpect(jsonPath("$.ok").value(false))
                                .andReturn();

                String validResponseContentGetId = validRequestResultGetId.getResponse().getContentAsString();
                System.out.println("validResponseContentGetId: " + validRequestResultGetId);
                System.out.println("validResponseContentGetId: " + validResponseContentGetId);
        }

        @Test
        @Rollback
        @Transactional
        public void testAddShop_ShopNameDuplicateMismatch() throws Exception {

                MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/register/captcha")
                                .session((MockHttpSession) session)
                                .param("timestamp", "1234567891"))
                                .andExpect(status().isOk())
                                .andReturn();

                session = mvcResult.getRequest().getSession();
                captchaText = (String) session.getAttribute("captchaText");

                JSONObject requestBody = new JSONObject();
                requestBody.put("shopName", "Test Shop");
                requestBody.put("phone", "1234567890");
                requestBody.put("description", "Test Description");
                requestBody.put("addressId", 1);
                requestBody.put("addressDetail", "888888");
                requestBody.put("captcha", captchaText);

                mockMvc.perform(post("/shop/register")
                                .session((MockHttpSession) session)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody.toString())
                                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                                .andExpect(status().isOk())
                                .andReturn();

                MvcResult mvcResultMismatch = mockMvc.perform(MockMvcRequestBuilders.get("/register/captcha")
                                .session((MockHttpSession) session)
                                .param("timestamp", "1234567891"))
                                .andExpect(status().isOk())
                                .andReturn();

                session = mvcResultMismatch.getRequest().getSession();
                captchaText = (String) session.getAttribute("captchaText");
                requestBody.put("shopName", "Test Shop");
                requestBody.put("phone", "1234567890");
                requestBody.put("description", "Test Description");
                requestBody.put("addressId", 1);
                requestBody.put("addressDetail", "888888");
                requestBody.put("captcha", captchaText);
                MvcResult validRequestResultGetId = mockMvc.perform(post("/shop/register")
                                .session((MockHttpSession) session)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody.toString())
                                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.code").value(701)) // 验证返回的 JSON 中的 code 字段值为 402
                                .andExpect(jsonPath("$.message").value("商店名稱重複"))
                                .andExpect(jsonPath("$.ok").value(false))
                                .andReturn();

                String validResponseContentGetId = validRequestResultGetId.getResponse().getContentAsString();
                System.out.println("validResponseContentGetId: " + validRequestResultGetId);
                System.out.println("validResponseContentGetId: " + validResponseContentGetId);
        }

}
