package com.order_lunch.controller.UserControllerTest;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

@SpringBootTest
@AutoConfigureMockMvc // 自動配置 MockMvc
@ActiveProfiles("test") // 加載 application-test.properties 文件配置
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserRegisterControllerTest {
        @Autowired
        private MockMvc mockMvc;

        private HttpSession session;

        private String captchaText;

        @Autowired
        private ObjectMapper objectMapper;

        @BeforeEach
        void setUp() throws Exception {
                MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/register/captcha")
                                .param("timestamp", "1234567890"))
                                .andExpect(status().isOk())
                                .andReturn();
                session = mvcResult.getRequest().getSession();
                captchaText = (String) session.getAttribute("captchaText");

        }

        @Test
        void testRegister() throws Exception {

                JSONObject validUserRequest = new JSONObject();
                validUserRequest.put("name", "John");
                validUserRequest.put("account", "john123@gmail.com");
                validUserRequest.put("password", "password");
                validUserRequest.put("passwordCheck", "password");
                validUserRequest.put("verifyCode", captchaText);

                mockMvc.perform(post("/user/register")
                                .session((MockHttpSession) session)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(validUserRequest.toString().getBytes())
                                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                                .andExpect(status().isOk())
                                .andReturn();
        }

        @Test
        void testRegisterDataError() throws Exception {
                String userRequest = new JSONObject()
                                .put("name", "")
                                .put("account", "")
                                .put("password", "")
                                .put("passwordCheck", "")
                                .put("captcha", "")
                                .toString();
                MvcResult validRequestResult = mockMvc.perform(MockMvcRequestBuilders.post("/user/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(userRequest)
                                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                                .andExpect(status().isBadRequest())
                                .andExpect(result -> {
                                        String nameError = JsonPath.read(result.getResponse().getContentAsString(),
                                                        "$.name");
                                        assertTrue("must not be blank".equals(nameError)
                                                        || "size must be between 3 and 32".equals(nameError));
                                })
                                .andExpect(MockMvcResultMatchers.jsonPath("$.account").value(Matchers.anyOf(
                                                Matchers.is("must be a well-formed email address"),
                                                Matchers.is("must not be blank"),
                                                Matchers.is("size must be between 4 and 64"))))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.password").value(Matchers.anyOf(
                                                Matchers.is("must not be blank"),
                                                Matchers.is("size must be between 8 and 16"))))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.passwordCheck").value(Matchers.anyOf(
                                                Matchers.is("must not be blank"),
                                                Matchers.is("size must be between 8 and 16"))))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.verifyCode").value(Matchers.anyOf(
                                                Matchers.is("must not be blank"),
                                                Matchers.is("size must be between 4 and 4"))))
                                .andReturn();

                String validResponseContent = validRequestResult.getResponse().getContentAsString();
                System.out.println("validRequestResult: " + validRequestResult);
                System.out.println("validResponseContent: " + validResponseContent);

        }

        @Test
        void testRegisterCaptchaError() throws Exception {

                JSONObject validUserRequest = new JSONObject();
                validUserRequest.put("name", "John");
                validUserRequest.put("account", "john123@gmail.com");
                validUserRequest.put("password", "password");
                validUserRequest.put("passwordCheck", "password");
                validUserRequest.put("captcha", "");

                mockMvc.perform(post("/user/register")
                                .session((MockHttpSession) session)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(validUserRequest.toString().getBytes())
                                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                                .andExpect(status().isBadRequest())
                                .andReturn();
        }

        
}
