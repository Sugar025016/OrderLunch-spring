package com.order_lunch.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc // 自動配置 MockMvc
@ActiveProfiles("test") // 加載 application-test.properties 文件配置
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CategoryControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @Test
    void testDeleteController() throws Exception {

    }

    @Test
    void testGetController() throws Exception {
        int id = 1;
        MvcResult validRequestResult = mockMvc.perform(get("/category/{id}", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("便當"))
                .andReturn();
        String validResponseContent = validRequestResult.getResponse().getContentAsString();
        System.out.println("validRequestResult: " + validRequestResult);
        System.out.println("validResponseContent: " + validResponseContent);
    }

    @Test
    void testGetControllers() throws Exception {

        MvcResult validRequestResult = mockMvc.perform(get("/category")
                // .session((MockHttpSession) session)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())

                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("便當"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("火鍋"))
                .andExpect(jsonPath("$[2].id").value(3))
                .andExpect(jsonPath("$[2].name").value("炸雞"))
                .andExpect(jsonPath("$[3].id").value(4))
                .andExpect(jsonPath("$[3].name").value("手搖飲"))
                .andReturn();
        String validResponseContent = validRequestResult.getResponse().getContentAsString();
        System.out.println("validRequestResult: " + validRequestResult);
        System.out.println("validResponseContent: " + validResponseContent);
    }
}
