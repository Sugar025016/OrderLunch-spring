package com.order_lunch.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.servlet.http.HttpSession;

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
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc // 自動配置 MockMvc
@ActiveProfiles("test") // 加載 application-test.properties 文件配置
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CartControllerTest {

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
    @Rollback
    @Transactional
    void testDeleteCart() throws Exception {
        int cartId = 6;
        mockMvc.perform(delete("/cart/" + cartId)
                .session((MockHttpSession) session)
                .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk());

        MvcResult validRequestResult = mockMvc.perform(get("/cart")
                .session((MockHttpSession) session)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.shopId").value(1))
                .andExpect(jsonPath("$.shopName").value("valueA"))
                .andExpect(jsonPath("$.deliveryKm").value(10.1))
                .andExpect(jsonPath("$.deliveryPrice").value(300))
                .andExpect(jsonPath("$.schedules[0].week").value(0))
                .andExpect(jsonPath("$.schedules[0].timePeriods[0].startTime").value("00:00"))
                .andExpect(jsonPath("$.schedules[0].timePeriods[0].endTime").value("00:00"))
                .andExpect(jsonPath("$.schedules[1].week").value(1))
                .andExpect(jsonPath("$.schedules[1].timePeriods[0].startTime").value("00:20"))
                .andExpect(jsonPath("$.schedules[1].timePeriods[0].endTime").value("00:50"))
                .andExpect(jsonPath("$.schedules[1].timePeriods[1].startTime").value("03:20"))
                .andExpect(jsonPath("$.schedules[1].timePeriods[1].endTime").value("06:30"))
                .andExpect(jsonPath("$.schedules[2].week").value(2))
                .andExpect(jsonPath("$.schedules[2].timePeriods[0].startTime").value("00:20"))
                .andExpect(jsonPath("$.schedules[2].timePeriods[0].endTime").value("00:50"))
                .andExpect(jsonPath("$.schedules[2].timePeriods[1].startTime").value("03:20"))
                .andExpect(jsonPath("$.schedules[2].timePeriods[1].endTime").value("06:30"))
                .andExpect(jsonPath("$.schedules[3].week").value(3))
                .andExpect(jsonPath("$.schedules[3].timePeriods[0].startTime").value("00:20"))
                .andExpect(jsonPath("$.schedules[3].timePeriods[0].endTime").value("00:50"))
                .andExpect(jsonPath("$.schedules[3].timePeriods[1].startTime").value("03:20"))
                .andExpect(jsonPath("$.schedules[3].timePeriods[1].endTime").value("06:30"))
                .andExpect(jsonPath("$.schedules[4].week").value(4))
                .andExpect(jsonPath("$.schedules[4].timePeriods[0].startTime").value("00:00"))
                .andExpect(jsonPath("$.schedules[4].timePeriods[0].endTime").value("00:00"))
                .andExpect(jsonPath("$.schedules[5].week").value(5))
                .andExpect(jsonPath("$.schedules[5].timePeriods[0].startTime").value("00:10"))
                .andExpect(jsonPath("$.schedules[5].timePeriods[0].endTime").value("04:20"))
                .andExpect(jsonPath("$.schedules[5].timePeriods[1].startTime").value("09:10"))
                .andExpect(jsonPath("$.schedules[5].timePeriods[1].endTime").value("15:30"))
                .andExpect(jsonPath("$.schedules[6].week").value(6))
                .andExpect(jsonPath("$.schedules[6].timePeriods").isEmpty())
                .andExpect(jsonPath("$.orderable").value(true))
                .andExpect(jsonPath("$.cartResponses[0].department").value(""))
                .andExpect(jsonPath("$.cartResponses[0].orderUsername").value("userName"))
                .andExpect(jsonPath("$.cartResponses[0].qty").value(5))
                .andExpect(jsonPath("$.cartResponses[0].remark").isEmpty())
                .andExpect(jsonPath("$.cartResponses[0].productResponse.productId").value(2))
                .andExpect(jsonPath("$.cartResponses[0].productResponse.productName").value("value1BBCC"))
                .andExpect(jsonPath("$.cartResponses[0].productResponse.price").value(200.0))
                .andExpect(jsonPath("$.cartResponses[0].productResponse.orderable").value(false))
                .andExpect(jsonPath("$.cartResponses[0].cartId").value(2))
                .andReturn();
        String validResponseContent = validRequestResult.getResponse().getContentAsString();
        System.out.println("validRequestResult: " + validRequestResult);
        System.out.println("validResponseContent: " + validResponseContent);

    }

    @Test
    void testGetCart() throws Exception {

        // Perform GET request and validate the response
        MvcResult validRequestResult = mockMvc.perform(get("/cart")
                .session((MockHttpSession) session)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.shopId").value(1))
                .andExpect(jsonPath("$.shopName").value("valueA"))
                .andExpect(jsonPath("$.deliveryKm").value(10.1))
                .andExpect(jsonPath("$.deliveryPrice").value(300))
                .andExpect(jsonPath("$.schedules[0].week").value(0))
                .andExpect(jsonPath("$.schedules[0].timePeriods[0].startTime").value("00:00"))
                .andExpect(jsonPath("$.schedules[0].timePeriods[0].endTime").value("00:00"))
                .andExpect(jsonPath("$.schedules[1].week").value(1))
                .andExpect(jsonPath("$.schedules[1].timePeriods[0].startTime").value("00:20"))
                .andExpect(jsonPath("$.schedules[1].timePeriods[0].endTime").value("00:50"))
                .andExpect(jsonPath("$.schedules[1].timePeriods[1].startTime").value("03:20"))
                .andExpect(jsonPath("$.schedules[1].timePeriods[1].endTime").value("06:30"))
                .andExpect(jsonPath("$.schedules[2].week").value(2))
                .andExpect(jsonPath("$.schedules[2].timePeriods[0].startTime").value("00:20"))
                .andExpect(jsonPath("$.schedules[2].timePeriods[0].endTime").value("00:50"))
                .andExpect(jsonPath("$.schedules[2].timePeriods[1].startTime").value("03:20"))
                .andExpect(jsonPath("$.schedules[2].timePeriods[1].endTime").value("06:30"))
                .andExpect(jsonPath("$.schedules[3].week").value(3))
                .andExpect(jsonPath("$.schedules[3].timePeriods[0].startTime").value("00:20"))
                .andExpect(jsonPath("$.schedules[3].timePeriods[0].endTime").value("00:50"))
                .andExpect(jsonPath("$.schedules[3].timePeriods[1].startTime").value("03:20"))
                .andExpect(jsonPath("$.schedules[3].timePeriods[1].endTime").value("06:30"))
                .andExpect(jsonPath("$.schedules[4].week").value(4))
                .andExpect(jsonPath("$.schedules[4].timePeriods[0].startTime").value("00:00"))
                .andExpect(jsonPath("$.schedules[4].timePeriods[0].endTime").value("00:00"))
                .andExpect(jsonPath("$.schedules[5].week").value(5))
                .andExpect(jsonPath("$.schedules[5].timePeriods[0].startTime").value("00:10"))
                .andExpect(jsonPath("$.schedules[5].timePeriods[0].endTime").value("04:20"))
                .andExpect(jsonPath("$.schedules[5].timePeriods[1].startTime").value("09:10"))
                .andExpect(jsonPath("$.schedules[5].timePeriods[1].endTime").value("15:30"))
                .andExpect(jsonPath("$.schedules[6].week").value(6))
                .andExpect(jsonPath("$.schedules[6].timePeriods").isEmpty())
                .andExpect(jsonPath("$.orderable").value(true))
                .andExpect(jsonPath("$.cartResponses[0].department").value(""))
                .andExpect(jsonPath("$.cartResponses[0].orderUsername").value("userName"))
                .andExpect(jsonPath("$.cartResponses[0].qty").value(5))
                .andExpect(jsonPath("$.cartResponses[0].remark").isEmpty())
                .andExpect(jsonPath("$.cartResponses[0].productResponse.productId").value(2))
                .andExpect(jsonPath("$.cartResponses[0].productResponse.productName").value("value1BBCC"))
                .andExpect(jsonPath("$.cartResponses[0].productResponse.price").value(200.0))
                .andExpect(jsonPath("$.cartResponses[0].productResponse.orderable").value(false))
                .andExpect(jsonPath("$.cartResponses[0].cartId").value(2))
                .andExpect(jsonPath("$.cartResponses[1].department").value(""))
                .andExpect(jsonPath("$.cartResponses[1].orderUsername").value("userName"))
                .andExpect(jsonPath("$.cartResponses[1].qty").value(3))
                .andExpect(jsonPath("$.cartResponses[1].remark").isEmpty())
                .andExpect(jsonPath("$.cartResponses[1].productResponse.productId").value(4))
                .andExpect(jsonPath("$.cartResponses[1].productResponse.productName").value("value1D"))
                .andExpect(jsonPath("$.cartResponses[1].productResponse.price").value(40.0))
                .andExpect(jsonPath("$.cartResponses[1].productResponse.orderable").value(true))
                .andExpect(jsonPath("$.cartResponses[1].cartId").value(6))
                .andReturn();
        String validResponseContent = validRequestResult.getResponse().getContentAsString();
        System.out.println("validRequestResult: " + validRequestResult);
        System.out.println("validResponseContent: " + validResponseContent);

    }

    @Test
    @Rollback
    @Transactional
    void testAddCart() throws Exception {
        JSONObject cartRequestJson = new JSONObject();
        cartRequestJson.put("productId", 1);
        cartRequestJson.put("department", "department");
        cartRequestJson.put("orderUsername", "orderUsername");
        cartRequestJson.put("qty", 5);
        cartRequestJson.put("remark", "remark");
        mockMvc.perform(post("/cart")
                .session((MockHttpSession) session)
                .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .content(cartRequestJson.toString()))
                .andExpect(status().isOk())
                .andExpect(content().string("13"))
                .andReturn();

        MvcResult validRequestResult = mockMvc.perform(get("/cart")
                .session((MockHttpSession) session)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.shopId").value(1))
                .andExpect(jsonPath("$.shopName").value("valueA"))
                .andExpect(jsonPath("$.deliveryKm").value(10.1))
                .andExpect(jsonPath("$.deliveryPrice").value(300))
                .andExpect(jsonPath("$.schedules[0].week").value(0))
                .andExpect(jsonPath("$.schedules[0].timePeriods[0].startTime").value("00:00"))
                .andExpect(jsonPath("$.schedules[0].timePeriods[0].endTime").value("00:00"))
                .andExpect(jsonPath("$.schedules[1].week").value(1))
                .andExpect(jsonPath("$.schedules[1].timePeriods[0].startTime").value("00:20"))
                .andExpect(jsonPath("$.schedules[1].timePeriods[0].endTime").value("00:50"))
                .andExpect(jsonPath("$.schedules[1].timePeriods[1].startTime").value("03:20"))
                .andExpect(jsonPath("$.schedules[1].timePeriods[1].endTime").value("06:30"))
                .andExpect(jsonPath("$.schedules[2].week").value(2))
                .andExpect(jsonPath("$.schedules[2].timePeriods[0].startTime").value("00:20"))
                .andExpect(jsonPath("$.schedules[2].timePeriods[0].endTime").value("00:50"))
                .andExpect(jsonPath("$.schedules[2].timePeriods[1].startTime").value("03:20"))
                .andExpect(jsonPath("$.schedules[2].timePeriods[1].endTime").value("06:30"))
                .andExpect(jsonPath("$.schedules[3].week").value(3))
                .andExpect(jsonPath("$.schedules[3].timePeriods[0].startTime").value("00:20"))
                .andExpect(jsonPath("$.schedules[3].timePeriods[0].endTime").value("00:50"))
                .andExpect(jsonPath("$.schedules[3].timePeriods[1].startTime").value("03:20"))
                .andExpect(jsonPath("$.schedules[3].timePeriods[1].endTime").value("06:30"))
                .andExpect(jsonPath("$.schedules[4].week").value(4))
                .andExpect(jsonPath("$.schedules[4].timePeriods[0].startTime").value("00:00"))
                .andExpect(jsonPath("$.schedules[4].timePeriods[0].endTime").value("00:00"))
                .andExpect(jsonPath("$.schedules[5].week").value(5))
                .andExpect(jsonPath("$.schedules[5].timePeriods[0].startTime").value("00:10"))
                .andExpect(jsonPath("$.schedules[5].timePeriods[0].endTime").value("04:20"))
                .andExpect(jsonPath("$.schedules[5].timePeriods[1].startTime").value("09:10"))
                .andExpect(jsonPath("$.schedules[5].timePeriods[1].endTime").value("15:30"))
                .andExpect(jsonPath("$.schedules[6].week").value(6))
                .andExpect(jsonPath("$.schedules[6].timePeriods").isEmpty())
                .andExpect(jsonPath("$.orderable").value(true))
                .andExpect(jsonPath("$.cartResponses[0].department").value(""))
                .andExpect(jsonPath("$.cartResponses[0].orderUsername").value("userName"))
                .andExpect(jsonPath("$.cartResponses[0].qty").value(5))
                .andExpect(jsonPath("$.cartResponses[0].remark").isEmpty())
                .andExpect(jsonPath("$.cartResponses[0].productResponse.productId").value(2))
                .andExpect(jsonPath("$.cartResponses[0].productResponse.productName").value("value1BBCC"))
                .andExpect(jsonPath("$.cartResponses[0].productResponse.price").value(200.0))
                .andExpect(jsonPath("$.cartResponses[0].productResponse.orderable").value(false))
                .andExpect(jsonPath("$.cartResponses[0].cartId").value(2))
                .andExpect(jsonPath("$.cartResponses[1].department").value(""))
                .andExpect(jsonPath("$.cartResponses[1].orderUsername").value("userName"))
                .andExpect(jsonPath("$.cartResponses[1].qty").value(3))
                .andExpect(jsonPath("$.cartResponses[1].remark").isEmpty())
                .andExpect(jsonPath("$.cartResponses[1].productResponse.productId").value(4))
                .andExpect(jsonPath("$.cartResponses[1].productResponse.productName").value("value1D"))
                .andExpect(jsonPath("$.cartResponses[1].productResponse.price").value(40.0))
                .andExpect(jsonPath("$.cartResponses[1].productResponse.orderable").value(true))
                .andExpect(jsonPath("$.cartResponses[1].cartId").value(6))
                .andExpect(jsonPath("$.cartResponses[2].department").value("department"))
                .andExpect(jsonPath("$.cartResponses[2].orderUsername").value("orderUsername"))
                .andExpect(jsonPath("$.cartResponses[2].qty").value(5))
                .andExpect(jsonPath("$.cartResponses[2].remark").value("remark"))
                .andExpect(jsonPath("$.cartResponses[2].productResponse.productId").value(1))
                .andExpect(jsonPath("$.cartResponses[2].productResponse.productName").value("value1A---"))
                .andExpect(jsonPath("$.cartResponses[2].productResponse.price").value(100.0))
                .andExpect(jsonPath("$.cartResponses[2].productResponse.orderable").value(true))
                .andExpect(jsonPath("$.cartResponses[2].cartId").value(10))
                .andReturn();
        String validResponseContent = validRequestResult.getResponse().getContentAsString();
        System.out.println("validRequestResult: " + validRequestResult);
        System.out.println("validResponseContent: " + validResponseContent);

    }

    @Test
    @Rollback
    @Transactional
    void testAddCart_NewShop() throws Exception {
        JSONObject cartRequestJson = new JSONObject();
        cartRequestJson.put("productId", 11);
        cartRequestJson.put("department", "department");
        cartRequestJson.put("orderUsername", "orderUsername");
        cartRequestJson.put("qty", 5);
        cartRequestJson.put("remark", "remark");
        mockMvc.perform(post("/cart")
                .session((MockHttpSession) session)
                .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .content(cartRequestJson.toString()))
                .andExpect(status().isOk())
                .andExpect(content().string("5"))
                .andReturn();

        MvcResult validRequestResult = mockMvc.perform(get("/cart")
                .session((MockHttpSession) session)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.shopId").value(2))
                .andExpect(jsonPath("$.shopName").value("valueB"))
                .andExpect(jsonPath("$.deliveryKm").value(8.0))
                .andExpect(jsonPath("$.deliveryPrice").value(0))
                .andExpect(jsonPath("$.schedules[0].week").value(0))
                .andExpect(jsonPath("$.schedules[0].timePeriods").isEmpty())
                .andExpect(jsonPath("$.schedules[1].week").value(1))
                .andExpect(jsonPath("$.schedules[1].timePeriods[0].startTime").value("00:30"))
                .andExpect(jsonPath("$.schedules[1].timePeriods[0].endTime").value("03:40"))
                .andExpect(jsonPath("$.schedules[1].timePeriods[1].startTime").value("10:00"))
                .andExpect(jsonPath("$.schedules[1].timePeriods[1].endTime").value("18:10"))
                .andExpect(jsonPath("$.schedules[2].week").value(2))
                .andExpect(jsonPath("$.schedules[2].timePeriods").isEmpty())
                .andExpect(jsonPath("$.schedules[3].week").value(3))
                .andExpect(jsonPath("$.schedules[3].timePeriods").isEmpty())
                .andExpect(jsonPath("$.schedules[4].week").value(4))
                .andExpect(jsonPath("$.schedules[4].timePeriods").isEmpty())
                .andExpect(jsonPath("$.schedules[5].week").value(5))
                .andExpect(jsonPath("$.schedules[5].timePeriods[0].startTime").value("13:00"))
                .andExpect(jsonPath("$.schedules[5].timePeriods[0].endTime").value("17:00"))
                .andExpect(jsonPath("$.schedules[6].week").value(6))
                .andExpect(jsonPath("$.schedules[6].timePeriods").isEmpty())
                .andExpect(jsonPath("$.cartResponses[0].department").value("department"))
                .andExpect(jsonPath("$.cartResponses[0].orderUsername").value("orderUsername"))
                .andExpect(jsonPath("$.cartResponses[0].qty").value(5))
                .andExpect(jsonPath("$.cartResponses[0].remark").value("remark"))
                .andExpect(jsonPath("$.cartResponses[0].productResponse.productId").value(11))
                .andExpect(jsonPath("$.cartResponses[0].productResponse.productName").value("VVVVVV"))
                .andExpect(jsonPath("$.cartResponses[0].productResponse.price").value(99999.0))
                .andExpect(jsonPath("$.cartResponses[0].productResponse.orderable").value(true))
                .andExpect(jsonPath("$.cartResponses[0].cartId").value(10))
                .andExpect(jsonPath("$.orderable").value(true))
                .andReturn();
        String validResponseContent = validRequestResult.getResponse().getContentAsString();
        System.out.println("validRequestResult: " + validRequestResult);
        System.out.println("validResponseContent: " + validResponseContent);

    }

    @Test
    @Rollback
    @Transactional
    void testAddCart_Mismatch() throws Exception {
        JSONObject cartRequestJson = new JSONObject();
        cartRequestJson.put("productId", 111);
        cartRequestJson.put("department", "department");
        cartRequestJson.put("orderUsername", "orderUsername");
        cartRequestJson.put("qty", 5);
        cartRequestJson.put("remark", "remark");
        MvcResult validRequestResult = mockMvc.perform(post("/cart")
                .session((MockHttpSession) session)
                .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .content(cartRequestJson.toString()))
                // .andExpect(status().isOk())
                .andReturn();

        String validResponseContent = validRequestResult.getResponse().getContentAsString();
        System.out.println("validRequestResult: " + validRequestResult);
        System.out.println("validResponseContent: " + validResponseContent);

    }

    @Test
    @Rollback
    @Transactional
    void testPutCart() throws Exception {
        int cartId = 2;
        int qty = 8;
        mockMvc.perform(put("/cart/" + cartId + "/" + qty)
                .session((MockHttpSession) session)
                .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andReturn();

        MvcResult validRequestResult = mockMvc.perform(get("/cart")
                .session((MockHttpSession) session)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.shopId").value(1))
                .andExpect(jsonPath("$.shopName").value("valueA"))
                .andExpect(jsonPath("$.deliveryKm").value(10.1))
                .andExpect(jsonPath("$.deliveryPrice").value(300))
                .andExpect(jsonPath("$.schedules[0].week").value(0))
                .andExpect(jsonPath("$.schedules[0].timePeriods[0].startTime").value("00:00"))
                .andExpect(jsonPath("$.schedules[0].timePeriods[0].endTime").value("00:00"))
                .andExpect(jsonPath("$.schedules[1].week").value(1))
                .andExpect(jsonPath("$.schedules[1].timePeriods[0].startTime").value("00:20"))
                .andExpect(jsonPath("$.schedules[1].timePeriods[0].endTime").value("00:50"))
                .andExpect(jsonPath("$.schedules[1].timePeriods[1].startTime").value("03:20"))
                .andExpect(jsonPath("$.schedules[1].timePeriods[1].endTime").value("06:30"))
                .andExpect(jsonPath("$.schedules[2].week").value(2))
                .andExpect(jsonPath("$.schedules[2].timePeriods[0].startTime").value("00:20"))
                .andExpect(jsonPath("$.schedules[2].timePeriods[0].endTime").value("00:50"))
                .andExpect(jsonPath("$.schedules[2].timePeriods[1].startTime").value("03:20"))
                .andExpect(jsonPath("$.schedules[2].timePeriods[1].endTime").value("06:30"))
                .andExpect(jsonPath("$.schedules[3].week").value(3))
                .andExpect(jsonPath("$.schedules[3].timePeriods[0].startTime").value("00:20"))
                .andExpect(jsonPath("$.schedules[3].timePeriods[0].endTime").value("00:50"))
                .andExpect(jsonPath("$.schedules[3].timePeriods[1].startTime").value("03:20"))
                .andExpect(jsonPath("$.schedules[3].timePeriods[1].endTime").value("06:30"))
                .andExpect(jsonPath("$.schedules[4].week").value(4))
                .andExpect(jsonPath("$.schedules[4].timePeriods[0].startTime").value("00:00"))
                .andExpect(jsonPath("$.schedules[4].timePeriods[0].endTime").value("00:00"))
                .andExpect(jsonPath("$.schedules[5].week").value(5))
                .andExpect(jsonPath("$.schedules[5].timePeriods[0].startTime").value("00:10"))
                .andExpect(jsonPath("$.schedules[5].timePeriods[0].endTime").value("04:20"))
                .andExpect(jsonPath("$.schedules[5].timePeriods[1].startTime").value("09:10"))
                .andExpect(jsonPath("$.schedules[5].timePeriods[1].endTime").value("15:30"))
                .andExpect(jsonPath("$.schedules[6].week").value(6))
                .andExpect(jsonPath("$.schedules[6].timePeriods").isEmpty())
                .andExpect(jsonPath("$.orderable").value(true))
                .andExpect(jsonPath("$.cartResponses[0].department").value(""))
                .andExpect(jsonPath("$.cartResponses[0].orderUsername").value("userName"))
                .andExpect(jsonPath("$.cartResponses[0].qty").value(8))
                .andExpect(jsonPath("$.cartResponses[0].remark").isEmpty())
                .andExpect(jsonPath("$.cartResponses[0].productResponse.productId").value(2))
                .andExpect(jsonPath("$.cartResponses[0].productResponse.productName").value("value1BBCC"))
                .andExpect(jsonPath("$.cartResponses[0].productResponse.price").value(200.0))
                .andExpect(jsonPath("$.cartResponses[0].productResponse.orderable").value(false))
                .andExpect(jsonPath("$.cartResponses[0].cartId").value(2))
                .andExpect(jsonPath("$.cartResponses[1].department").value(""))
                .andExpect(jsonPath("$.cartResponses[1].orderUsername").value("userName"))
                .andExpect(jsonPath("$.cartResponses[1].qty").value(3))
                .andExpect(jsonPath("$.cartResponses[1].remark").isEmpty())
                .andExpect(jsonPath("$.cartResponses[1].productResponse.productId").value(4))
                .andExpect(jsonPath("$.cartResponses[1].productResponse.productName").value("value1D"))
                .andExpect(jsonPath("$.cartResponses[1].productResponse.price").value(40.0))
                .andExpect(jsonPath("$.cartResponses[1].productResponse.orderable").value(true))
                .andExpect(jsonPath("$.cartResponses[1].cartId").value(6))
                .andReturn();
        String validResponseContent = validRequestResult.getResponse().getContentAsString();
        System.out.println("validRequestResult: " + validRequestResult);
        System.out.println("validResponseContent: " + validResponseContent);

    }
}
