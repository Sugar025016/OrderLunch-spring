package com.order_lunch.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

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

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc // 自動配置 MockMvc
@ActiveProfiles("test") // 加載 application-test.properties 文件配置
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OrderControllerTest {

        private HttpSession session;

        private String captchaText;

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

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
        void testAddOrder() throws Exception {
                JSONObject orderRequest = new JSONObject();
                orderRequest.put("takeTime", LocalDateTime.now().plusDays(1).toString());
                orderRequest.put("addressId", 40);
                orderRequest.put("remark", "Test remark");

                MvcResult validRequestResult = mockMvc.perform(post("/order")
                                .session((MockHttpSession) session)
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(orderRequest.toString())
                                .principal(() -> "customUser"))
                                .andExpect(status().isOk())
                                .andReturn();
                String validResponseContent = validRequestResult.getResponse().getContentAsString();
                System.out.println("validRequestResult: " + validRequestResult);
                System.out.println("validResponseContent: " + validResponseContent);
        }

        @Test
        @Rollback
        @Transactional
        void testAddOrder_DeliveryKm() throws Exception {
                JSONObject orderRequest = new JSONObject();
                orderRequest.put("takeTime", LocalDateTime.now().plusDays(1).toString());
                orderRequest.put("addressId", 37);
                orderRequest.put("remark", "Test remark");

                MvcResult validRequestResult = mockMvc.perform(post("/order")
                                .session((MockHttpSession) session)
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(orderRequest.toString())
                                .principal(() -> "customUser"))
                                .andExpect(status().isBadRequest())
                                .andReturn();
                String validResponseContent = validRequestResult.getResponse().getContentAsString();
                System.out.println("validRequestResult: " + validRequestResult);
                System.out.println("validResponseContent: " + validResponseContent);
        }

        @Test
        void testGetNewOrderByUser() throws Exception {
                MvcResult validRequestResult = mockMvc.perform(get("/order/new")
                                .session((MockHttpSession) session)
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$[0].shopId").value(3))
                                .andExpect(jsonPath("$[0].orderId").value(65))
                                .andExpect(jsonPath("$[0].shopName").value("valueC"))
                                .andExpect(jsonPath("$[0].userName").value("user"))
                                .andExpect(jsonPath("$[0].description").value("description"))
                                .andExpect(jsonPath("$[0].imgUrl").value("http://localhost:8082/16932027106902359.jpg"))
                                .andExpect(jsonPath("$[0].totalPrice").value(99999))
                                .andExpect(jsonPath("$[0].remark").value(""))
                                .andExpect(jsonPath("$[0].statusChinese").value("等待店家接收中"))
                                .andExpect(jsonPath("$[0].status").value(11))
                                .andExpect(jsonPath("$[0].orderTime").value("2023-11-07T16:55:10.842628"))
                                .andExpect(jsonPath("$[0].takeTime").value("2023-11-07T19:30:00"))
                                .andExpect(jsonPath("$[0].address.id").value(11))
                                .andExpect(jsonPath("$[0].address.addressDataId").value(19639))
                                .andExpect(jsonPath("$[0].address.city").value("彰化縣"))
                                .andExpect(jsonPath("$[0].address.area").value("秀水鄉"))
                                .andExpect(jsonPath("$[0].address.street").value("三塊巷"))
                                .andExpect(jsonPath("$[0].address.detail").value("5555號"))
                                .andExpect(jsonPath("$[0].address.lat").value(24.0593315))
                                .andExpect(jsonPath("$[0].address.lng").value(120.4864467))
                                .andExpect(jsonPath("$[0].orderDetails[0].orderDetailId").value(128))
                                .andExpect(jsonPath("$[0].orderDetails[0].productId").value(11))
                                .andExpect(jsonPath("$[0].orderDetails[0].productName").value("VVVVVV"))
                                .andExpect(jsonPath("$[0].orderDetails[0].qty").value(1))
                                .andExpect(jsonPath("$[0].orderDetails[0].price").value(0))
                                .andExpect(jsonPath("$[0].orderDetails[0].remark").value(""))
                                .andExpect(jsonPath("$[0].orderDetails[0].status").value(0))
                                .andExpect(jsonPath("$[0].orderDetails[0].imgUrl")
                                                .value("http://localhost:8082/16937508048951735.JPG"))
                                .andReturn();
                String validResponseContent = validRequestResult.getResponse().getContentAsString();
                System.out.println("validRequestResult: " + validRequestResult);
                System.out.println("validResponseContent: " + validResponseContent);
        }

        @Test
        void testGetOrderByShop() throws Exception {

                int shopId = 3;
                MvcResult validRequestResult0 = mockMvc.perform(get("/order/sell/{shopId}", shopId)
                                .session((MockHttpSession) session)
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("classify", "0")
                                .param("page", "0")
                                .param("size", "5"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.content").isArray())
                                .andExpect(jsonPath("$.content.length()").value(2))
                                .andExpect(jsonPath("$.content[0].orderId").value(18))
                                .andExpect(jsonPath("$.content[0].shopName").value("valueC"))
                                .andExpect(jsonPath("$.content[0].userName").value("user"))
                                .andExpect(jsonPath("$.content[0].description").value("description"))
                                .andExpect(jsonPath("$.content[0].imgUrl")
                                                .value("http://localhost:8082/16932027106902359.jpg"))
                                .andExpect(jsonPath("$.content[0].totalPrice").value(0))
                                .andExpect(jsonPath("$.content[0].remark").value(""))
                                .andExpect(jsonPath("$.content[0].statusChinese").value("訂單完成"))
                                .andExpect(jsonPath("$.content[0].status").value(99))
                                .andExpect(jsonPath("$.content[0].orderTime").value("2023-11-03T04:17:31.452359"))
                                .andExpect(jsonPath("$.content[0].takeTime").value("2023-11-07T12:50:00"))
                                .andExpect(jsonPath("$.content[0].address.id").value(10))
                                .andExpect(jsonPath("$.content[0].address.addressDataId").value(4548))
                                .andExpect(jsonPath("$.content[0].address.city").value("宜蘭縣"))
                                .andExpect(jsonPath("$.content[0].address.area").value("礁溪鄉"))
                                .andExpect(jsonPath("$.content[0].address.street").value("二結路"))
                                .andExpect(jsonPath("$.content[0].address.detail").value("777---"))
                                .andExpect(jsonPath("$.content[0].address.lat").value(24.783683))
                                .andExpect(jsonPath("$.content[0].address.lng").value(121.6990149))
                                .andExpect(jsonPath("$.content[1].orderId").value(29))
                                .andExpect(jsonPath("$.content[1].shopName").value("valueC"))
                                .andExpect(jsonPath("$.content[1].userName").value("user"))
                                .andExpect(jsonPath("$.content[1].description").value("description"))
                                .andExpect(jsonPath("$.content[1].imgUrl")
                                                .value("http://localhost:8082/16932027106902359.jpg"))
                                .andExpect(jsonPath("$.content[1].totalPrice").value(40))
                                .andExpect(jsonPath("$.content[1].remark").value(""))
                                .andExpect(jsonPath("$.content[1].statusChinese").value("店家拒接單"))
                                .andExpect(jsonPath("$.content[1].status").value(93))
                                .andExpect(jsonPath("$.content[1].orderTime").value("2023-11-07T16:03:08.513778"))
                                .andExpect(jsonPath("$.content[1].takeTime").value("2023-11-07T17:10:00"))
                                .andExpect(jsonPath("$.content[1].address.id").value(12))
                                .andExpect(jsonPath("$.content[1].address.addressDataId").value(24480))
                                .andExpect(jsonPath("$.content[1].address.city").value("嘉義市"))
                                .andExpect(jsonPath("$.content[1].address.area").value("東區"))
                                .andExpect(jsonPath("$.content[1].address.street").value("大雅路２段"))
                                .andExpect(jsonPath("$.content[1].address.detail").value("東勢街101號"))
                                .andExpect(jsonPath("$.content[1].address.lat").value(23.4775897))
                                .andExpect(jsonPath("$.content[1].address.lng").value(120.4722109))
                                .andExpect(jsonPath("$.content[1].orderDetails[0].orderDetailId").value(51))
                                .andExpect(jsonPath("$.content[1].orderDetails[0].productId").value(10))
                                .andExpect(jsonPath("$.content[1].orderDetails[0].productName").value("value1D"))
                                .andExpect(jsonPath("$.content[1].orderDetails[0].qty").value(1))
                                .andExpect(jsonPath("$.content[1].orderDetails[0].price").value(0))
                                .andExpect(jsonPath("$.content[1].orderDetails[0].remark").value(""))
                                .andExpect(jsonPath("$.content[1].orderDetails[0].status").value(0))
                                .andExpect(jsonPath("$.content[1].orderDetails[0].imgUrl").isEmpty())
                                .andReturn();
                String validResponseContent0 = validRequestResult0.getResponse().getContentAsString();

                System.out.println("validRequestResult0: " + validRequestResult0);
                System.out.println("validResponseContent0: " + validResponseContent0);

                MvcResult validRequestResult1 = mockMvc.perform(get("/order/sell/{shopId}", shopId)
                                .session((MockHttpSession) session)
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("classify", "1")
                                .param("page", "0")
                                .param("size", "5"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.content[0].orderId").value(65))
                                .andExpect(jsonPath("$.content[0].shopName").value("valueC"))
                                .andExpect(jsonPath("$.content[0].userName").value("user"))
                                .andExpect(jsonPath("$.content[0].description").value("description"))
                                .andExpect(jsonPath("$.content[0].imgUrl")
                                                .value("http://localhost:8082/16932027106902359.jpg"))
                                .andExpect(jsonPath("$.content[0].totalPrice").value(99999))
                                .andExpect(jsonPath("$.content[0].statusChinese").value("等待店家接收中"))
                                .andExpect(jsonPath("$.content[0].status").value(11))
                                .andExpect(jsonPath("$.content[0].orderTime").value("2023-11-07T16:55:10.842628"))
                                .andExpect(jsonPath("$.content[0].takeTime").value("2023-11-07T19:30:00"))
                                .andExpect(jsonPath("$.content[0].address.id").value(11))
                                .andExpect(jsonPath("$.content[0].address.city").value("彰化縣"))
                                .andExpect(jsonPath("$.content[0].address.area").value("秀水鄉"))
                                .andExpect(jsonPath("$.content[0].address.street").value("三塊巷"))
                                .andExpect(jsonPath("$.content[0].address.detail").value("5555號"))
                                .andExpect(jsonPath("$.content[0].address.lat").value(24.0593315))
                                .andExpect(jsonPath("$.content[0].address.lng").value(120.4864467))
                                .andExpect(jsonPath("$.content[0].orderDetails[0].orderDetailId").value(128))
                                .andExpect(jsonPath("$.content[0].orderDetails[0].productId").value(11))
                                .andExpect(jsonPath("$.content[0].orderDetails[0].productName").value("VVVVVV"))
                                .andExpect(jsonPath("$.content[0].orderDetails[0].qty").value(1))
                                .andExpect(jsonPath("$.content[0].orderDetails[0].imgUrl")
                                                .value("http://localhost:8082/16937508048951735.JPG"))
                                .andExpect(jsonPath("$.pageable.pageNumber").value(0))
                                .andExpect(jsonPath("$.pageable.pageSize").value(5))
                                .andExpect(jsonPath("$.last").value(true))
                                .andExpect(jsonPath("$.totalPages").value(1))
                                .andExpect(jsonPath("$.totalElements").value(1))
                                .andExpect(jsonPath("$.first").value(true))
                                .andExpect(jsonPath("$.numberOfElements").value(1))
                                .andExpect(jsonPath("$.size").value(5))
                                .andExpect(jsonPath("$.number").value(0))
                                .andReturn();
                String validResponseContent1 = validRequestResult1.getResponse().getContentAsString();

                System.out.println("validRequestResult1: " + validRequestResult1);
                System.out.println("validResponseContent1: " + validResponseContent1);

                MvcResult validRequestResult2 = mockMvc.perform(get("/order/sell/{shopId}", shopId)
                                .session((MockHttpSession) session)
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("classify", "2")
                                .param("page", "0")
                                .param("size", "5"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.content").isArray())
                                .andExpect(jsonPath("$.content[0].orderId").value(31))
                                .andExpect(jsonPath("$.content[0].shopName").value("valueC"))
                                .andExpect(jsonPath("$.content[0].userName").value("user"))
                                .andExpect(jsonPath("$.content[0].description").value("description"))
                                .andExpect(jsonPath("$.content[0].imgUrl")
                                                .value("http://localhost:8082/16932027106902359.jpg"))
                                .andExpect(jsonPath("$.content[0].totalPrice").value(99999))
                                .andExpect(jsonPath("$.content[0].statusChinese").value("店家已接收"))
                                .andExpect(jsonPath("$.content[0].status").value(12))
                                .andExpect(jsonPath("$.content[0].orderTime").value("2023-11-07T16:41:34.82042"))
                                .andExpect(jsonPath("$.content[0].takeTime").value("2023-11-07T18:20:00"))
                                .andExpect(jsonPath("$.content[0].address.id").value(12))
                                .andExpect(jsonPath("$.content[0].address.city").value("嘉義市"))
                                .andExpect(jsonPath("$.content[0].address.area").value("東區"))
                                .andExpect(jsonPath("$.content[0].address.street").value("大雅路２段"))
                                .andExpect(jsonPath("$.content[0].address.detail").value("東勢街101號"))
                                .andExpect(jsonPath("$.content[0].address.lat").value(23.4775897))
                                .andExpect(jsonPath("$.content[0].address.lng").value(120.4722109))
                                .andExpect(jsonPath("$.content[0].orderDetails[0].orderDetailId").value(53))
                                .andExpect(jsonPath("$.content[0].orderDetails[0].productId").value(11))
                                .andExpect(jsonPath("$.content[0].orderDetails[0].productName").value("VVVVVV"))
                                .andExpect(jsonPath("$.content[0].orderDetails[0].qty").value(1))
                                .andExpect(jsonPath("$.content[0].orderDetails[0].imgUrl")
                                                .value("http://localhost:8082/16937508048951735.JPG"))

                                .andExpect(jsonPath("$.pageable.pageNumber").value(0))
                                .andExpect(jsonPath("$.pageable.pageSize").value(5))
                                .andExpect(jsonPath("$.last").value(true))
                                .andExpect(jsonPath("$.totalPages").value(1))
                                .andExpect(jsonPath("$.totalElements").value(1))
                                .andExpect(jsonPath("$.first").value(true))
                                .andExpect(jsonPath("$.numberOfElements").value(1))
                                .andExpect(jsonPath("$.size").value(5))
                                .andExpect(jsonPath("$.number").value(0))
                                .andReturn();
                String validResponseContent2 = validRequestResult2.getResponse().getContentAsString();

                System.out.println("validRequestResult2: " + validRequestResult2);
                System.out.println("validResponseContent2: " + validResponseContent2);

        }

        @Test
        void testGetOrderByUserPage() throws Exception {

                int page = 1;
                MvcResult validRequestResult = mockMvc.perform(get("/order/{page}", page)
                                .session((MockHttpSession) session)
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.content[0].shopId").value(1))
                                .andExpect(jsonPath("$.content[0].orderId").value(26))
                                .andExpect(jsonPath("$.content[0].shopName").value("valueA"))
                                .andExpect(jsonPath("$.content[0].userName").value("user"))
                                .andExpect(jsonPath("$.content[0].description").value("description55555555555666"))
                                .andExpect(jsonPath("$.content[0].imgUrl")
                                                .value("http://localhost:8082/17015922948981374.jpg"))
                                .andExpect(jsonPath("$.content[0].totalPrice").value(590))
                                .andExpect(jsonPath("$.content[0].statusChinese").value("店家已接收"))
                                .andExpect(jsonPath("$.content[0].status").value(12))
                                .andExpect(jsonPath("$.content[0].orderTime").value("2023-11-07T13:04:10.224239"))
                                .andExpect(jsonPath("$.content[0].takeTime").value("2023-11-13T01:00:00"))
                                .andExpect(jsonPath("$.content[0].address.city").value("嘉義市"))
                                .andExpect(jsonPath("$.content[1].shopId").value(2))
                                .andExpect(jsonPath("$.content[1].orderId").value(64))
                                .andExpect(jsonPath("$.content[1].shopName").value("valueB"))
                                .andExpect(jsonPath("$.content[1].userName").value("user"))
                                .andExpect(jsonPath("$.content[1].description").value("description商店介紹"))
                                .andExpect(jsonPath("$.content[1].imgUrl")
                                                .value("http://localhost:8082/1691994708919221.jpg"))
                                .andExpect(jsonPath("$.content[1].totalPrice").value(500))
                                .andExpect(jsonPath("$.content[1].statusChinese").value("店家已接收"))
                                .andExpect(jsonPath("$.content[1].status").value(12))
                                .andExpect(jsonPath("$.content[1].orderTime").value("2023-11-07T16:54:55.003852"))
                                .andExpect(jsonPath("$.content[1].takeTime").value("2023-11-07T19:40:00"))
                                .andExpect(jsonPath("$.content[1].address.city").value("嘉義市"))
                                .andExpect(jsonPath("$.content[2].shopId").value(3))
                                .andExpect(jsonPath("$.content[2].orderId").value(65))
                                .andExpect(jsonPath("$.content[2].shopName").value("valueC"))
                                .andExpect(jsonPath("$.content[2].userName").value("user"))
                                .andExpect(jsonPath("$.content[2].description").value("description"))
                                .andExpect(jsonPath("$.content[2].imgUrl")
                                                .value("http://localhost:8082/16932027106902359.jpg"))
                                .andExpect(jsonPath("$.content[2].totalPrice").value(99999))
                                .andExpect(jsonPath("$.content[2].statusChinese").value("等待店家接收中"))
                                .andExpect(jsonPath("$.content[2].status").value(11))
                                .andExpect(jsonPath("$.content[2].orderTime").value("2023-11-07T16:55:10.842628"))
                                .andExpect(jsonPath("$.content[2].takeTime").value("2023-11-07T19:30:00"))
                                .andExpect(jsonPath("$.content[2].address.city").value("彰化縣"))
                                .andExpect(jsonPath("$.totalPages").value(6))
                                .andExpect(jsonPath("$.totalElements").value(56))
                                .andExpect(jsonPath("$.first").value(true))
                                .andExpect(jsonPath("$.size").value(10))
                                .andReturn();
                String validResponseContent = validRequestResult.getResponse().getContentAsString();

                System.out.println("validRequestResult: " + validRequestResult);
                System.out.println("validResponseContent: " + validResponseContent);

        }

        @Test
        @Rollback
        @Transactional
        void testPutOrderByShop() throws Exception {
                int shopId = 3;
                int status = 12;
                List<Integer> orderIds = Arrays.asList(65);
                MvcResult validRequestResult = mockMvc.perform(put("/order/{shop}/{status}", shopId, status)
                                .principal(() -> "username")
                                .contentType("application/json")
                                .session((MockHttpSession) session)
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                                .content(objectMapper.writeValueAsString(orderIds)))
                                .andExpect(status().isOk())
                                // .andExpect(content().string("true"))
                                .andReturn();
                String validResponseContent = validRequestResult.getResponse().getContentAsString();

                System.out.println("validRequestResult: " + validRequestResult);
                System.out.println("validResponseContent: " + validResponseContent);

        }

        @Test
        @Rollback
        @Transactional
        void testPutOrderStatus() throws Exception {
                int status = 12;
                List<Integer> orderIds = Arrays.asList(65);
                MvcResult validRequestResult = mockMvc.perform(put("/order/{status}", status)
                                .principal(() -> "username")
                                .contentType("application/json")
                                .session((MockHttpSession) session)
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                                .content(objectMapper.writeValueAsString(orderIds)))
                                .andExpect(status().isOk())
                                .andReturn();

                String validResponseContent = validRequestResult.getResponse().getContentAsString();
                System.out.println("validRequestResult: " + validRequestResult);
                System.out.println("validResponseContent: " + validResponseContent);
        }
}
