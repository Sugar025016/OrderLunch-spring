package com.order_lunch.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import net.minidev.json.JSONObject;

@SpringBootTest
@AutoConfigureMockMvc // 自動配置 MockMvc
@ActiveProfiles("test") // 加載 application-test.properties 文件配置
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AddressControllerTest {

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
        void testGetAddress() throws Exception {

                MvcResult validRequestResult = mockMvc.perform(get("/address")
                                .session((MockHttpSession) session)
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$[0].id").value(37))
                                .andExpect(jsonPath("$[0].addressDataId").value(1009))
                                .andExpect(jsonPath("$[0].city").value("基隆市"))
                                .andExpect(jsonPath("$[0].area").value("中正區"))
                                .andExpect(jsonPath("$[0].street").value("中船路"))
                                .andExpect(jsonPath("$[0].detail").value("123號"))
                                .andExpect(jsonPath("$[0].lat").value(25.1377761))
                                .andExpect(jsonPath("$[0].lng").value(121.7512333))
                                .andExpect(jsonPath("$[1].id").value(38)) // 你可以添加更多的驗證
                                .andExpect(jsonPath("$[1].addressDataId").value(1009))
                                .andExpect(jsonPath("$[1].city").value("基隆市"))
                                .andExpect(jsonPath("$[1].area").value("中正區"))
                                .andExpect(jsonPath("$[1].street").value("中船路"))
                                .andExpect(jsonPath("$[1].detail").value("123巷5號"))
                                .andExpect(jsonPath("$[1].lat").value(25.1377761))
                                .andExpect(jsonPath("$[1].lng").value(121.7512333))
                                // 持續添加更多驗證...
                                .andExpect(jsonPath("$[2].id").value(39))
                                .andExpect(jsonPath("$[2].addressDataId").value(4548))
                                .andExpect(jsonPath("$[2].city").value("宜蘭縣"))
                                .andExpect(jsonPath("$[2].area").value("礁溪鄉"))
                                .andExpect(jsonPath("$[2].street").value("二結路"))
                                .andExpect(jsonPath("$[2].detail").value("777巷22號5樓之8"))
                                .andExpect(jsonPath("$[2].lat").value(24.783683))
                                .andExpect(jsonPath("$[2].lng").value(121.6990149))
                                .andExpect(jsonPath("$[3].id").value(40))
                                .andExpect(jsonPath("$[3].addressDataId").value(28906))
                                .andExpect(jsonPath("$[3].city").value("台南市"))
                                .andExpect(jsonPath("$[3].area").value("南區"))
                                .andExpect(jsonPath("$[3].street").value("大同路２段"))
                                .andExpect(jsonPath("$[3].detail").value("123號"))
                                .andExpect(jsonPath("$[3].lat").value(22.9779755))
                                .andExpect(jsonPath("$[3].lng").value(120.2136988))
                                .andReturn();
                String validResponseContent = validRequestResult.getResponse().getContentAsString();
                System.out.println("validRequestResult: " + validRequestResult);
                System.out.println("validResponseContent: " + validResponseContent);
        }

        @Test
        @Rollback
        @Transactional
        void testAddAddress() throws Exception {
                JSONObject addressRequest = new JSONObject();
                addressRequest.put("city", "台南市");
                addressRequest.put("area", "中西區");
                addressRequest.put("street", "大仁街");
                addressRequest.put("detail", "123號");

                mockMvc.perform(post("/address")
                                .session((MockHttpSession) session)
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(addressRequest.toString()))
                                .andExpect(status().isOk())
                                .andReturn();

                MvcResult validRequestResult = mockMvc.perform(get("/address")
                                .session((MockHttpSession) session)
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$").isArray())
                                .andExpect(jsonPath("$", hasSize(6)))
                                .andExpect(jsonPath("$[0].lng").value(120.192348))
                                .andExpect(jsonPath("$[0].id").value(49))
                                .andExpect(jsonPath("$[0].addressDataId").value(28544))
                                .andExpect(jsonPath("$[0].city").value("台南市"))
                                .andExpect(jsonPath("$[0].area").value("中西區"))
                                .andExpect(jsonPath("$[0].street").value("大仁街"))
                                .andExpect(jsonPath("$[0].detail").value("123號"))
                                .andExpect(jsonPath("$[0].lat").value(22.9906233))
                                .andExpect(jsonPath("$[0].lng").value(120.192348))
                                .andReturn();
                String validResponseContent = validRequestResult.getResponse().getContentAsString();
                System.out.println("validRequestResult: " + validRequestResult);
                System.out.println("validResponseContent: " + validResponseContent);

        }

        // 打算刪除這隻api
        // @Test
        // @Rollback
        // @Transactional
        // void testPutAddress() throws Exception {

        // JSONObject addressRequest = new JSONObject();
        // addressRequest.put("city", "台南市");
        // addressRequest.put("area", "中西區");
        // addressRequest.put("street", "大仁街");
        // addressRequest.put("detail", "123號");

        // mockMvc.perform(post("/address")
        // .session((MockHttpSession) session)
        // .with(SecurityMockMvcRequestPostProcessors.csrf())
        // .contentType(MediaType.APPLICATION_JSON)
        // .content(addressRequest.toString()))
        // .andExpect(status().isOk())
        // .andReturn();

        // MvcResult validRequestResult = mockMvc.perform(get("/address")
        // .session((MockHttpSession) session)
        // .contentType(MediaType.APPLICATION_JSON))
        // .andExpect(status().isOk())
        // .andExpect(jsonPath("$").isArray())
        // .andExpect(jsonPath("$", hasSize(6)))
        // .andExpect(jsonPath("$[0].lng").value(120.192348))
        // .andExpect(jsonPath("$[0].id").value(49))
        // .andExpect(jsonPath("$[0].addressDataId").value(28544))
        // .andExpect(jsonPath("$[0].city").value("台南市"))
        // .andExpect(jsonPath("$[0].area").value("中西區"))
        // .andExpect(jsonPath("$[0].street").value("大仁街"))
        // .andExpect(jsonPath("$[0].detail").value("123號"))
        // .andExpect(jsonPath("$[0].lat").value(22.9906233))
        // .andExpect(jsonPath("$[0].lng").value(120.192348))
        // .andReturn();
        // String validResponseContent =
        // validRequestResult.getResponse().getContentAsString();
        // System.out.println("validRequestResult: " + validRequestResult);
        // System.out.println("validResponseContent: " + validResponseContent);

        // }

        @Test
        @Rollback
        @Transactional
        void testDeleteAddress() throws Exception {

                int addressId = 40;

                mockMvc.perform(delete("/address/{addressId}", addressId)
                                .session((MockHttpSession) session)
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andReturn();

                MvcResult validRequestResult = mockMvc.perform(get("/address")
                                .session((MockHttpSession) session)
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$[0].id").value(37))
                                .andExpect(jsonPath("$[0].addressDataId").value(1009))
                                .andExpect(jsonPath("$[0].city").value("基隆市"))
                                .andExpect(jsonPath("$[0].area").value("中正區"))
                                .andExpect(jsonPath("$[0].street").value("中船路"))
                                .andExpect(jsonPath("$[0].detail").value("123號"))
                                .andExpect(jsonPath("$[0].lat").value(25.1377761))
                                .andExpect(jsonPath("$[0].lng").value(121.7512333))
                                .andExpect(jsonPath("$[1].id").value(38)) // 你可以添加更多的驗證
                                .andExpect(jsonPath("$[1].addressDataId").value(1009))
                                .andExpect(jsonPath("$[1].city").value("基隆市"))
                                .andExpect(jsonPath("$[1].area").value("中正區"))
                                .andExpect(jsonPath("$[1].street").value("中船路"))
                                .andExpect(jsonPath("$[1].detail").value("123巷5號"))
                                .andExpect(jsonPath("$[1].lat").value(25.1377761))
                                .andExpect(jsonPath("$[1].lng").value(121.7512333))
                                // 持續添加更多驗證...
                                .andExpect(jsonPath("$[2].id").value(39))
                                .andExpect(jsonPath("$[2].addressDataId").value(4548))
                                .andExpect(jsonPath("$[2].city").value("宜蘭縣"))
                                .andExpect(jsonPath("$[2].area").value("礁溪鄉"))
                                .andExpect(jsonPath("$[2].street").value("二結路"))
                                .andExpect(jsonPath("$[2].detail").value("777巷22號5樓之8"))
                                .andExpect(jsonPath("$[2].lat").value(24.783683))
                                .andExpect(jsonPath("$[2].lng").value(121.6990149))
                                .andReturn();
                String validResponseContent = validRequestResult.getResponse().getContentAsString();
                System.out.println("validRequestResult: " + validRequestResult);
                System.out.println("validResponseContent: " + validResponseContent);

        }

}
