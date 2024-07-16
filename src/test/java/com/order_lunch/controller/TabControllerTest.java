package com.order_lunch.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import javax.servlet.http.HttpSession;

import org.hamcrest.Matchers;
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

import net.minidev.json.JSONObject;

@SpringBootTest
@AutoConfigureMockMvc // 自動配置 MockMvc
@ActiveProfiles("test") // 加載 application-test.properties 文件配置
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TabControllerTest {

    private HttpSession session;

    private String captchaText;

    @Autowired
    private MockMvc mockMvc;


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
    void testGetTabProducts() throws Exception {

        MvcResult validRequestResult = mockMvc.perform(get("/tab/1")
                .session((MockHttpSession) session)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(3))
                .andExpect(jsonPath("$[0].name").value("value1C"))
                .andExpect(jsonPath("$[0].shelve").value(true))
                .andExpect(jsonPath("$[0].products[0].id").value(1))
                .andExpect(jsonPath("$[0].products[0].name").value("value1A---"))
                .andExpect(jsonPath("$[0].products[0].description").value("12345"))
                .andExpect(jsonPath("$[0].products[0].imgUrl")
                        .value("http://localhost:8082/16927208415324378.jpg"))
                .andExpect(jsonPath("$[0].products[0].price").value(100))
                .andExpect(jsonPath("$[0].products[0].shopId").value(1))
                .andExpect(jsonPath("$[0].products[0].orderable").value(true))
                // 繼續對其他 products 進行驗證...
                .andExpect(jsonPath("$[1].id").value(34))
                .andExpect(jsonPath("$[1].name").value("大特價"))
                .andExpect(jsonPath("$[1].shelve").value(true))
                .andExpect(jsonPath("$[1].products[0].id").value(1))
                .andExpect(jsonPath("$[1].products[0].name").value("value1A---"))
                .andExpect(jsonPath("$[1].products[0].description").value("12345"))
                .andExpect(jsonPath("$[1].products[0].imgUrl")
                        .value("http://localhost:8082/16927208415324378.jpg"))
                .andExpect(jsonPath("$[1].products[0].price").value(100))
                .andExpect(jsonPath("$[1].products[0].shopId").value(1))
                .andExpect(jsonPath("$[1].products[0].orderable").value(true))
                // 繼續對其他 products 進行驗證...
                .andExpect(jsonPath("$[2].id").value(35))
                .andExpect(jsonPath("$[2].name").value("便當"))
                .andExpect(jsonPath("$[2].shelve").value(true))
                .andExpect(jsonPath("$[2].products[0].id").value(1))
                .andExpect(jsonPath("$[2].products[0].name").value("value1A---"))
                .andExpect(jsonPath("$[2].products[0].description").value("12345"))
                .andExpect(jsonPath("$[2].products[0].imgUrl")
                        .value("http://localhost:8082/16927208415324378.jpg"))
                .andExpect(jsonPath("$[2].products[0].price").value(100))
                .andExpect(jsonPath("$[2].products[0].shopId").value(1))
                .andExpect(jsonPath("$[2].products[0].orderable").value(true))
                .andReturn();
        String validResponseContent = validRequestResult.getResponse().getContentAsString();
        System.out.println("validRequestResult: " + validRequestResult);
        System.out.println("validResponseContent: " + validResponseContent);

    }

    @Test
    @Rollback
    @Transactional
    void testPutTabProducts() throws Exception {

        JSONObject tabProductRequestJson = new JSONObject();
        tabProductRequestJson.put("shopId", 3);
        tabProductRequestJson.put("name", "Test Tab");
        tabProductRequestJson.put("shelve", true);
        tabProductRequestJson.put("productIds", Arrays.asList(15, 16));
        int tabId = 7;

        mockMvc.perform(put("/tab/{tabId}", tabId)
                .session((MockHttpSession) session)
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(tabProductRequestJson.toString()))
                .andExpect(status().isOk())
                .andReturn();

        MvcResult validRequestResult = mockMvc.perform(get("/shop/sell/3")
                .session((MockHttpSession) session)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tabProducts", hasSize(2))) // 驗證 tabProducts 陣列的大小为 3
                .andExpect(jsonPath("$.tabProducts[0].id", is(7))) // 驗證第一個 tabProduct 的 id 為 7
                .andExpect(jsonPath("$.tabProducts[0].name", is("Test Tab"))) // 驗證第一個 tabProduct 的 name
                                                                              // 為 "3333333"
                .andExpect(jsonPath("$.tabProducts[0].products", hasSize(2))) // 驗證第一個 tabProduct 的
                                                                              // products 陣列大小為 1
                .andExpect(jsonPath("$.tabProducts[0].products[0].id", is(15))) // 驗證第一個 tabProduct 的第一個
                                                                                // product 的
                .andExpect(jsonPath("$.tabProducts[0].products[1].id", is(16))) // 驗證第一個 tabProduct 的第一個
                                                                                // product 的
                                                                                // name 為 "AAA"
                .andExpect(jsonPath("$.tabProducts[1].id", is(8))) // 驗證第二個 tabProduct 的 id 為 8，以此類推
                .andExpect(jsonPath("$.tabProducts[1].name", is("value1C")))
                .andExpect(jsonPath("$.tabProducts[1].products", hasSize(1)))
                .andReturn();
        String validResponseContent = validRequestResult.getResponse().getContentAsString();
        System.out.println("validRequestResult: " + validRequestResult);
        System.out.println("validResponseContent: " + validResponseContent);
    }

    @Test
    @Rollback
    @Transactional
    void testPostTabProducts_Success() throws Exception {
        JSONObject tabProductRequestJson = new JSONObject();
        tabProductRequestJson.put("shopId", 3);
        tabProductRequestJson.put("name", "Test Tab");
        tabProductRequestJson.put("shelve", true);
        tabProductRequestJson.put("productIds", Arrays.asList());

        mockMvc.perform(post("/tab")
                .session((MockHttpSession) session)
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(tabProductRequestJson.toString()))
                .andExpect(status().isOk())
                .andReturn();

        MvcResult validRequestResult = mockMvc.perform(get("/shop/sell/3")
                .session((MockHttpSession) session)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tabProducts", hasSize(3))) // 驗證 tabProducts 陣列的大小为 3
                .andExpect(jsonPath("$.tabProducts[0].id", is(7))) // 驗證第一個 tabProduct 的 id 為 7
                .andExpect(jsonPath("$.tabProducts[0].name", is("3333333"))) // 驗證第一個 tabProduct 的 name
                                                                             // 為 "3333333"
                .andExpect(jsonPath("$.tabProducts[0].products", hasSize(1))) // 驗證第一個 tabProduct 的
                                                                              // products 陣列大小為 1
                .andExpect(jsonPath("$.tabProducts[0].products[0].name", is("AAA"))) // 驗證第一個 tabProduct
                                                                                     // 的第一個 product 的
                                                                                     // name 為 "AAA"
                .andExpect(jsonPath("$.tabProducts[1].id", is(8))) // 驗證第二個 tabProduct 的 id 為 8，以此類推
                .andExpect(jsonPath("$.tabProducts[1].name", is("value1C")))
                .andExpect(jsonPath("$.tabProducts[1].products", hasSize(1)))
                .andExpect(jsonPath("$.tabProducts[2].id", is(40))) // 驗證第三個 tabProduct 的 id 為 38
                .andExpect(jsonPath("$.tabProducts[2].name", is("Test Tab")))
                .andExpect(jsonPath("$.tabProducts[2].products", hasSize(0)))
                .andReturn();
        String validResponseContent = validRequestResult.getResponse().getContentAsString();
        System.out.println("validRequestResult: " + validRequestResult);
        System.out.println("validResponseContent: " + validResponseContent);

    }

    @Test
    @Rollback
    @Transactional
    void testPostTabProducts_Mismatch() throws Exception {
        JSONObject tabProductRequestJson = new JSONObject();
        tabProductRequestJson.put("shopId", null);
        tabProductRequestJson.put("name", "");
        tabProductRequestJson.put("shelve", true);
        tabProductRequestJson.put("productIds", null);

        MvcResult validRequestResult = mockMvc.perform(post("/tab")
                .session((MockHttpSession) session)
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(tabProductRequestJson.toString()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.productIds", is("must not be null")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(Matchers.anyOf(
                        Matchers.is("must not be blank"),
                        Matchers.is("size must be between 3 and 16"))))
                .andExpect(jsonPath("$.shopId", is("must not be null")))
                .andReturn();

        String validResponseContent = validRequestResult.getResponse().getContentAsString();
        System.out.println("validRequestResult: " + validRequestResult);
        System.out.println("validResponseContent: " + validResponseContent);

    }

    @Test
    @Rollback
    @Transactional
    void testDeleteTabProduct() throws Exception {

        int tabId = 7;

        mockMvc.perform(delete("/tab/{tabId}", tabId)
                .session((MockHttpSession) session)
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        MvcResult validRequestResult = mockMvc.perform(get("/shop/sell/3")
                .session((MockHttpSession) session)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tabProducts", hasSize(1)))
                .andExpect(jsonPath("$.tabProducts[0].id", is(8)))
                .andExpect(jsonPath("$.tabProducts[0].name", is("value1C")))
                .andExpect(jsonPath("$.tabProducts[0].products", hasSize(1)))
                .andReturn();
        String validResponseContent = validRequestResult.getResponse().getContentAsString();
        System.out.println("validRequestResult: " + validRequestResult);
        System.out.println("validResponseContent: " + validResponseContent);

    }
}
