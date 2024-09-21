package com.order_lunch.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc // 自動配置 MockMvc
@ActiveProfiles("test") // 加載 application-test.properties 文件配置
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DemoTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetLogin() throws Exception {
        mockMvc.perform(post("/test"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetLogin2() throws Exception {
        mockMvc.perform(get("/test"))
                .andExpect(status().isOk());
    }

    @Test
    void testUploadFile() {

    }
}
