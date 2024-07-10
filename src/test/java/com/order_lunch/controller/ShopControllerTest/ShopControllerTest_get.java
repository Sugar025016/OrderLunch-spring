package com.order_lunch.controller.ShopControllerTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.hasSize;

import javax.servlet.http.HttpSession;

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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.order_lunch.controller.ShopController;

@SpringBootTest
@AutoConfigureMockMvc // 自動配置 MockMvc
@ActiveProfiles("test") // 加載 application-test.properties 文件配置
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ShopControllerTest_get {

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

        // H2沒有mysql的ST_Distance_Sphere(計算兩個地理坐標點之間的球面距離)函數，無法測試
        @Test
        public void testGetShops() throws Exception {

                mockMvc.perform(get("/logout")
                                .session((MockHttpSession) session))
                                .andExpect(status().isOk());
                MvcResult validRequestResult = mockMvc.perform(get("/shop")
                                .session((MockHttpSession) session)
                                .param("page", "0")
                                .param("size", "20"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.totalPages").value(1))
                                .andExpect(jsonPath("$.totalElements").value(11))
                                .andReturn();
                String validResponseContent = validRequestResult.getResponse().getContentAsString();
                System.out.println("validRequestResult: " + validRequestResult);
                System.out.println("validResponseContent: " + validResponseContent);
        }

        @Test
        public void testGetShops_withCategoryId() throws Exception {

                MvcResult validRequestResult = mockMvc.perform(get("/shop")
                                .session((MockHttpSession) session)
                                .param("categoryId", "1")
                                .param("page", "0")
                                .param("size", "20"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.totalElements").value(3))
                                .andReturn();
                String validResponseContent = validRequestResult.getResponse().getContentAsString();
                System.out.println("validRequestResult: " + validRequestResult);
                System.out.println("validResponseContent: " + validResponseContent);
        }

        @Test
        public void testGetShops_withOther() throws Exception {

                MvcResult validRequestResult = mockMvc.perform(get("/shop")
                                .session((MockHttpSession) session)
                                .param("other", "valueA")
                                .param("page", "0")
                                .param("size", "20"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.totalElements").value(1))
                                .andExpect(jsonPath("$.content[0].name").value("valueA"))
                                .andReturn();
                String validResponseContent = validRequestResult.getResponse().getContentAsString();
                System.out.println("validRequestResult: " + validRequestResult);
                System.out.println("validResponseContent: " + validResponseContent);
        }

        @Test
        public void testGetShops_withAddress() throws Exception {

                MvcResult validRequestResult = mockMvc.perform(get("/shop")
                                .session((MockHttpSession) session)
                                .param("city", "台南市")
                                .param("area", "東區")
                                .param("page", "0")
                                .param("size", "20"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.totalElements").value(5))
                                .andReturn();
                String validResponseContent = validRequestResult.getResponse().getContentAsString();
                System.out.println("validRequestResult: " + validRequestResult);
                System.out.println("validResponseContent: " + validResponseContent);
        }

        @Test
        public void testGetShop() throws Exception {
                // Mock data
                int id = 1;

                MvcResult validRequestResult = mockMvc.perform(get("/shop/" + id)
                                .session((MockHttpSession) session)
                                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(1))
                                .andExpect(jsonPath("$.name").value("valueA"))
                                .andExpect(jsonPath("$.address").value("台南市東區懷恩街999"))
                                .andExpect(jsonPath("$.schedules[0].week").value(0))
                                .andExpect(jsonPath("$.schedules[0].timePeriods[0].startTime").value("00:00:00"))
                                .andExpect(jsonPath("$.schedules[0].timePeriods[0].endTime").value("00:00:00"))
                                .andExpect(jsonPath("$.schedules[1].week").value(1))
                                .andExpect(jsonPath("$.schedules[1].timePeriods[0].startTime").value("00:20:00"))
                                .andExpect(jsonPath("$.schedules[1].timePeriods[0].endTime").value("00:50:00"))
                                .andExpect(jsonPath("$.schedules[1].timePeriods[1].startTime").value("03:20:00"))
                                .andExpect(jsonPath("$.schedules[1].timePeriods[1].endTime").value("06:30:00"))
                                .andExpect(jsonPath("$.schedules[2].week").value(2))
                                .andExpect(jsonPath("$.schedules[2].timePeriods[0].startTime").value("00:20:00"))
                                .andExpect(jsonPath("$.schedules[2].timePeriods[0].endTime").value("00:50:00"))
                                .andExpect(jsonPath("$.schedules[2].timePeriods[1].startTime").value("03:20:00"))
                                .andExpect(jsonPath("$.schedules[2].timePeriods[1].endTime").value("06:30:00"))
                                .andExpect(jsonPath("$.schedules[3].week").value(3))
                                .andExpect(jsonPath("$.schedules[3].timePeriods[0].startTime").value("00:20:00"))
                                .andExpect(jsonPath("$.schedules[3].timePeriods[0].endTime").value("00:50:00"))
                                .andExpect(jsonPath("$.schedules[3].timePeriods[1].startTime").value("03:20:00"))
                                .andExpect(jsonPath("$.schedules[3].timePeriods[1].endTime").value("06:30:00"))
                                .andExpect(jsonPath("$.schedules[4].week").value(4))
                                .andExpect(jsonPath("$.schedules[4].timePeriods[0].startTime").value("00:00:00"))
                                .andExpect(jsonPath("$.schedules[4].timePeriods[0].endTime").value("00:00:00"))
                                .andExpect(jsonPath("$.schedules[5].week").value(5))
                                .andExpect(jsonPath("$.schedules[5].timePeriods[0].startTime").value("00:10:00"))
                                .andExpect(jsonPath("$.schedules[5].timePeriods[0].endTime").value("04:20:00"))
                                .andExpect(jsonPath("$.schedules[5].timePeriods[1].startTime").value("09:10:00"))
                                .andExpect(jsonPath("$.schedules[5].timePeriods[1].endTime").value("15:30:00"))
                                .andExpect(jsonPath("$.schedules[6].week").value(6))
                                .andExpect(jsonPath("$.schedules[6].timePeriods").isEmpty())
                                .andReturn();
                String validResponseContent = validRequestResult.getResponse().getContentAsString();
                System.out.println("validRequestResult: " + validRequestResult);
                System.out.println("validResponseContent: " + validResponseContent);
        }

        @Test
        public void testGetShopName() throws Exception {

                MvcResult validRequestResult = mockMvc.perform(get("/shop/sell")
                                .session((MockHttpSession) session))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$[0].id").value(3))
                                .andExpect(jsonPath("$[0].name").value("valueC"))
                                .andExpect(jsonPath("$[1].id").value(4))
                                .andExpect(jsonPath("$[1].name").value("valueD"))
                                .andExpect(jsonPath("$[2].id").value(5))
                                .andExpect(jsonPath("$[2].name").value("newNAME"))
                                .andExpect(jsonPath("$[3].id").value(6))
                                .andExpect(jsonPath("$[3].name").value("AA"))
                                .andReturn();
                String validResponseContent = validRequestResult.getResponse().getContentAsString();
                System.out.println("validRequestResult: " + validRequestResult);
                System.out.println("validResponseContent: " + validResponseContent);
        }

        @Test
        public void testGetSellShop() throws Exception {
                // Mock data
                int id = 3;

                MvcResult validRequestResult = mockMvc.perform(get("/shop/sell/" + id)
                                .session((MockHttpSession) session)
                                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(3))
                                .andExpect(jsonPath("$.name").value("valueC"))
                                .andExpect(jsonPath("$.deliveryKm").value(10.0))
                                .andExpect(jsonPath("$.deliveryPrice").value(0))
                                .andExpect(jsonPath("$.description").value("description"))
                                .andExpect(jsonPath("$.imgId").value(15))
                                .andExpect(jsonPath("$.imgUrl").value("http://localhost:8082/16932027106902359.jpg"))
                                .andExpect(jsonPath("$.phone").value("1234567890"))
                                .andExpect(jsonPath("$.schedules[0].week").value(0))
                                .andExpect(jsonPath("$.schedules[0].timePeriods[0].startTime").value("08:00"))
                                .andExpect(jsonPath("$.schedules[0].timePeriods[0].endTime").value("12:00"))
                                .andExpect(jsonPath("$.address.city").value("台南市"))
                                .andExpect(jsonPath("$.address.area").value("永康區"))
                                .andExpect(jsonPath("$.address.street").value("永正路"))
                                .andExpect(jsonPath("$.address.detail").value("36巷5號"))
                                .andExpect(jsonPath("$.address.lat").value(23.0266901))
                                .andExpect(jsonPath("$.address.lng").value(120.2541104))
                                .andExpect(jsonPath("$.tabProducts").isEmpty())
                                .andExpect(jsonPath("$.products", hasSize(2)))
                                .andReturn();
                String validResponseContent = validRequestResult.getResponse().getContentAsString();
                System.out.println("validRequestResult: " + validRequestResult);
                System.out.println("validResponseContent: " + validResponseContent);
        }
}
