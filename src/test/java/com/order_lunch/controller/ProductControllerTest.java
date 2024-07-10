package com.order_lunch.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
public class ProductControllerTest {

    private HttpSession session;

    private String captchaText;

    @Autowired
    private MockMvc mockMvc;

    // @InjectMocks
    // private ShopController shopController;

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
    void testGetProducts() throws Exception {

        int id = 1;
        // Perform GET request and validate the response
        MvcResult validRequestResult = mockMvc.perform(get("/product/{shopId}", id)
                .session((MockHttpSession) session)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("value1A---"))
                .andExpect(jsonPath("$[0].description").value("12345"))
                .andExpect(jsonPath("$[0].price").value(100))
                .andExpect(jsonPath("$[0].shopId").value(1))
                .andExpect(jsonPath("$[0].orderable").value(true))

                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("value1BBCC"))
                .andExpect(jsonPath("$[1].description").value("1234566666666555"))
                .andExpect(jsonPath("$[1].price").value(200))
                .andExpect(jsonPath("$[1].shopId").value(1))
                .andExpect(jsonPath("$[1].orderable").value(false))
                .andReturn();
        String validResponseContent = validRequestResult.getResponse().getContentAsString();
        System.out.println("validRequestResult: " + validRequestResult);
        System.out.println("validResponseContent: " + validResponseContent);

    }

    @Test
    void testGetSellProducts() throws Exception {
        int id = 3;
        // Perform GET request and validate the response
        MvcResult validRequestResult = mockMvc.perform(get("/product/sell/{shopId}", id)
                .session((MockHttpSession) session)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(15)) // 验证第一个product的id为15
                .andExpect(jsonPath("$[0].name").value("AAA")) // 验证第一个product的name为"AAA"
                .andExpect(jsonPath("$[0].description").value("FFFF")) // 验证第一个product的description为"FFFF"
                .andExpect(jsonPath("$[0].price").value(123)) // 验证第一个product的price为123
                .andExpect(jsonPath("$[0].imgUrl").value("http://localhost:8082/16939174106862309.JPG")) // 验证第一个product的imgUrl
                .andExpect(jsonPath("$[0].orderable").value(true))
                .andExpect(jsonPath("$[1].id").value(16)) // 验证第二个product的id为16
                .andExpect(jsonPath("$[1].name").value("UUUUUUUUUU")) // 验证第二个product的name为"UUUUUUUUUU"
                .andExpect(jsonPath("$[1].description").value("UUUUUUU")) // 验证第二个product的description为"UUUUUUU"
                .andExpect(jsonPath("$[1].price").value(123)) // 验证第二个product的price为123
                .andExpect(jsonPath("$[1].imgUrl").value("http://localhost:8082/16939174834635530.jpg")) // 验证第二个product的imgUrl
                .andExpect(jsonPath("$[1].orderable").value(true))
                .andReturn();
        String validResponseContent = validRequestResult.getResponse().getContentAsString();
        System.out.println("validRequestResult: " + validRequestResult);
        System.out.println("validResponseContent: " + validResponseContent);
    }

    @Test
    void testGetSellProducts_Null() throws Exception {
        int id = 1;
        // Perform GET request and validate the response
        MvcResult validRequestResult = mockMvc.perform(get("/product/sell/{shopId}", id)
                .session((MockHttpSession) session)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty())
                .andReturn();
        String validResponseContent = validRequestResult.getResponse().getContentAsString();
        System.out.println("validRequestResult: " + validRequestResult);
        System.out.println("validResponseContent: " + validResponseContent);
    }

    @Test
    @Rollback
    @Transactional
    void testDeleteProduct() throws Exception {
        int id = 16;
        // Perform GET request and validate the response
        MvcResult validRequestResult = mockMvc.perform(delete("/product/{productId}", id)
                .session((MockHttpSession) session)
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String validResponseContent = validRequestResult.getResponse().getContentAsString();
        System.out.println("validRequestResult: " + validRequestResult);
        System.out.println("validResponseContent: " + validResponseContent);
    }

    @Test
    @Rollback
    @Transactional
    void testPostProduct() throws Exception {

        JSONObject requestJson = new JSONObject();
        requestJson.put("name", "Test Product");
        requestJson.put("description", "Test description");
        requestJson.put("price", 100);
        requestJson.put("orderable", true);
        requestJson.put("imgId", 1);
        requestJson.put("imgUrl", "http://example.com/image.jpg");
        // Perform POST request
        MvcResult postValidRequestResult = mockMvc.perform(post("/product/{shopId}", 3)
                .session((MockHttpSession) session)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson.toString())
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())

                // .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(true))
                .andReturn();
        String postValidResponseContent = postValidRequestResult.getResponse().getContentAsString();
        System.out.println("validRequestResult: " + postValidRequestResult);
        System.out.println("validResponseContent: " + postValidResponseContent);

        int id = 3;
        // Perform GET request and validate the response
        MvcResult validRequestResult = mockMvc.perform(get("/product/sell/{shopId}", id)
                .session((MockHttpSession) session)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(15)) // 验证第一个product的id为15
                .andExpect(jsonPath("$[0].name").value("AAA")) // 验证第一个product的name为"AAA"
                .andExpect(jsonPath("$[0].description").value("FFFF")) // 验证第一个product的description为"FFFF"
                .andExpect(jsonPath("$[0].price").value(123)) // 验证第一个product的price为123
                .andExpect(jsonPath("$[0].imgUrl").value("http://localhost:8082/16939174106862309.JPG")) // 验证第一个product的imgUrl
                .andExpect(jsonPath("$[0].imgId").value(28)) // 验证第二个product的imgUrl
                .andExpect(jsonPath("$[0].orderable").value(true))
                .andExpect(jsonPath("$[1].id").value(16)) // 验证第二个product的id为16
                .andExpect(jsonPath("$[1].name").value("UUUUUUUUUU")) // 验证第二个product的name为"UUUUUUUUUU"
                .andExpect(jsonPath("$[1].description").value("UUUUUUU")) // 验证第二个product的description为"UUUUUUU"
                .andExpect(jsonPath("$[1].price").value(123)) // 验证第二个product的price为123
                .andExpect(jsonPath("$[1].imgUrl").value("http://localhost:8082/16939174834635530.jpg")) // 验证第二个product的imgUrl
                .andExpect(jsonPath("$[1].imgId").value(30)) // 验证第二个product的imgUrl
                .andExpect(jsonPath("$[1].orderable").value(true))
                .andExpect(jsonPath("$[2].id").value(25)) // 验证第二个product的id为16
                .andExpect(jsonPath("$[2].name").value("Test Product")) // 验证第二个product的name为"UUUUUUUUUU"
                .andExpect(jsonPath("$[2].description").value("Test description")) // 验证第二个product的description为"UUUUUUU"
                .andExpect(jsonPath("$[2].price").value(100)) // 验证第二个product的price为123
                .andExpect(jsonPath("$[2].imgUrl").value("http://localhost:8082/1691994708919221.jpg")) // 验证第二个product的imgUrl
                .andExpect(jsonPath("$[2].imgId").value(1)) // 验证第二个product的imgUrl
                .andExpect(jsonPath("$[2].orderable").value(true))
                .andReturn();
        String validResponseContent = validRequestResult.getResponse().getContentAsString();
        System.out.println("validRequestResult: " + validRequestResult);
        System.out.println("validResponseContent: " + validResponseContent);

    }

    @Test
    @Rollback
    @Transactional
    void testPostProduct_Valid() throws Exception {

        JSONObject requestJson = new JSONObject();
        requestJson.put("name", "");
        requestJson.put("description",
                "Test descriptionTest descriptionTest descriptionTest descriptionTest descriptionTest descriptionTest descriptionTest descriptionTest descriptionTest descriptionTest descriptionTest descriptionTest descriptionTest descriptionTest descriptionTest descriptionTest descriptionTest descriptionTest  descriptionTest descriptionTest descriptionTest descriptionTest descriptionTest descriptionTest descriptionTest descriptionTest descriptionTest description");
        requestJson.put("price", null);
        requestJson.put("orderable", null);
        requestJson.put("imgId", 1);
        requestJson.put("imgUrl", "http://example.com/image.jpg");
        // Perform POST request
        MvcResult postValidRequestResult = mockMvc.perform(post("/product/{shopId}", 3)
                .session((MockHttpSession) session)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson.toString())
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(Matchers.anyOf(
                        Matchers.is("must not be blank"),
                        Matchers.is("size must be between 3 and 16"))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(Matchers.anyOf(
                        Matchers.is("size must be between 0 and 255"))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(Matchers.anyOf(
                        Matchers.is("must not be null"))))
                .andReturn();
        String postValidResponseContent = postValidRequestResult.getResponse().getContentAsString();
        System.out.println("validRequestResult: " + postValidRequestResult);
        System.out.println("validResponseContent: " + postValidResponseContent);

    }

    @Test
    @Rollback
    @Transactional
    void testPutProduct() throws Exception {

        JSONObject requestJson = new JSONObject();
        requestJson.put("name", "Test Product");
        requestJson.put("description", "Test description");
        requestJson.put("price", 100);
        requestJson.put("orderable", true);
        requestJson.put("imgId", 1);
        requestJson.put("imgUrl", "http://example.com/image.jpg");
        // Perform POST request
        MvcResult postValidRequestResult = mockMvc.perform(put("/product/{productId}", 15)
                .session((MockHttpSession) session)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson.toString())
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())

                // .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(true))
                .andReturn();
        String postValidResponseContent = postValidRequestResult.getResponse().getContentAsString();
        System.out.println("validRequestResult: " + postValidRequestResult);
        System.out.println("validResponseContent: " + postValidResponseContent);

        int id = 3;
        // Perform GET request and validate the response
        MvcResult validRequestResult = mockMvc.perform(get("/product/sell/{shopId}", id)
                .session((MockHttpSession) session)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(15)) // 验证第二个product的id为16
                .andExpect(jsonPath("$[0].name").value("Test Product")) // 验证第二个product的name为"UUUUUUUUUU"
                .andExpect(jsonPath("$[0].description").value("Test description")) // 验证第二个product的description为"UUUUUUU"
                .andExpect(jsonPath("$[0].price").value(100)) // 验证第二个product的price为123
                .andExpect(jsonPath("$[0].imgUrl").value("http://localhost:8082/1691994708919221.jpg")) // 验证第二个product的imgUrl
                .andExpect(jsonPath("$[0].imgId").value(1)) // 验证第二个product的imgUrl
                .andExpect(jsonPath("$[0].orderable").value(true))
                .andExpect(jsonPath("$[1].id").value(16)) // 验证第二个product的id为16
                .andExpect(jsonPath("$[1].name").value("UUUUUUUUUU")) // 验证第二个product的name为"UUUUUUUUUU"
                .andExpect(jsonPath("$[1].description").value("UUUUUUU")) // 验证第二个product的description为"UUUUUUU"
                .andExpect(jsonPath("$[1].price").value(123)) // 验证第二个product的price为123
                .andExpect(jsonPath("$[1].imgUrl").value("http://localhost:8082/16939174834635530.jpg")) // 验证第二个product的imgUrl
                .andExpect(jsonPath("$[1].imgId").value(30)) // 验证第二个product的imgUrl
                .andExpect(jsonPath("$[1].orderable").value(true))
                .andReturn();
        String validResponseContent = validRequestResult.getResponse().getContentAsString();
        System.out.println("validRequestResult: " + validRequestResult);
        System.out.println("validResponseContent: " + validResponseContent);

    }



    @Test
    @Rollback
    @Transactional
    void testPutProduct_Valid() throws Exception {

        JSONObject requestJson = new JSONObject();
        requestJson.put("name", "");
        requestJson.put("description",
                "Test descriptionTest descriptionTest descriptionTest descriptionTest descriptionTest descriptionTest descriptionTest descriptionTest descriptionTest descriptionTest descriptionTest descriptionTest descriptionTest descriptionTest descriptionTest descriptionTest descriptionTest descriptionTest  descriptionTest descriptionTest descriptionTest descriptionTest descriptionTest descriptionTest descriptionTest descriptionTest descriptionTest description");
        requestJson.put("price", null);
        requestJson.put("orderable", null);
        requestJson.put("imgId", 1);
        requestJson.put("imgUrl", "http://example.com/image.jpg");
        // Perform POST request
        
        MvcResult postValidRequestResult = mockMvc.perform(put("/product/{productId}", 15)
                .session((MockHttpSession) session)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson.toString())
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(Matchers.anyOf(
                        Matchers.is("must not be blank"),
                        Matchers.is("size must be between 3 and 16"))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(Matchers.anyOf(
                        Matchers.is("size must be between 0 and 255"))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(Matchers.anyOf(
                        Matchers.is("must not be null"))))
                .andReturn();
        String postValidResponseContent = postValidRequestResult.getResponse().getContentAsString();
        System.out.println("validRequestResult: " + postValidRequestResult);
        System.out.println("validResponseContent: " + postValidResponseContent);
    }
}
