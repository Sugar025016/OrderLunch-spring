package com.order_lunch.controller;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.hamcrest.Matchers;
import org.json.JSONArray;
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
public class SchedulesControllerTest {

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
    void testPutSchedule_0() throws Exception {

        JSONArray scheduleWeeks = new JSONArray();
        for (int week = 0; week <= 6; week++) {
            JSONArray timePeriods = new JSONArray();

            // JSONObject scheduleTime = new JSONObject();
            // timePeriods.put(scheduleTime);

            JSONObject scheduleWeek = new JSONObject();
            scheduleWeek.put("week", week);
            scheduleWeek.put("timePeriods", timePeriods);

            scheduleWeeks.put(scheduleWeek);
        }

        JSONObject schedulesRequest = new JSONObject();
        schedulesRequest.put("type", 0);
        schedulesRequest.put("schedules", scheduleWeeks);
        int shopId = 3;
        System.out.println("schedulesRequest:" + schedulesRequest);

        mockMvc.perform(put("/schedule/{shopId}", shopId)
                .session((MockHttpSession) session)
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(schedulesRequest.toString()))
                .andExpect(status().isOk())
                .andReturn();

        MvcResult validRequestResult = mockMvc.perform(get("/shop/sell/{shopId} ", shopId)
                .session((MockHttpSession) session)
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.schedules[0].week").value(0))
                .andExpect(jsonPath("$.schedules[0].timePeriods").isEmpty())
                .andExpect(jsonPath("$.schedules[1].week").value(1))
                .andExpect(jsonPath("$.schedules[1].timePeriods").isEmpty())
                .andExpect(jsonPath("$.schedules[2].week").value(2))
                .andExpect(jsonPath("$.schedules[2].timePeriods").isEmpty())
                .andExpect(jsonPath("$.schedules[3].week").value(3))
                .andExpect(jsonPath("$.schedules[3].timePeriods").isEmpty())
                .andExpect(jsonPath("$.schedules[4].week").value(4))
                .andExpect(jsonPath("$.schedules[4].timePeriods").isEmpty())
                .andExpect(jsonPath("$.schedules[5].week").value(5))
                .andExpect(jsonPath("$.schedules[5].timePeriods").isEmpty())
                .andExpect(jsonPath("$.schedules[6].week").value(6))
                .andExpect(jsonPath("$.schedules[6].timePeriods").isEmpty())
                .andReturn();

        String validResponseContent = validRequestResult.getResponse().getContentAsString();
        System.out.println("validRequestResult: " + validRequestResult);
        System.out.println("validResponseContent: " + validResponseContent);
    }

    @Test
    @Rollback
    @Transactional
    void testPutSchedule_24() throws Exception {

        JSONArray scheduleWeeks = new JSONArray();
        for (int week = 0; week <= 6; week++) {
            JSONArray timePeriods = new JSONArray();

            JSONObject scheduleTime = new JSONObject();

            scheduleTime.put("startTime", "00:00");
            scheduleTime.put("endTime", "24:00");
            timePeriods.put(scheduleTime);

            JSONObject scheduleWeek = new JSONObject();
            scheduleWeek.put("week", week);
            scheduleWeek.put("timePeriods", timePeriods);

            scheduleWeeks.put(scheduleWeek);
        }

        JSONObject schedulesRequest = new JSONObject();
        schedulesRequest.put("type", 0);
        schedulesRequest.put("schedules", scheduleWeeks);
        int shopId = 3;

        mockMvc.perform(put("/schedule/{shopId}", shopId)
                .session((MockHttpSession) session)
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(schedulesRequest.toString()))
                .andExpect(status().isOk())
                .andReturn();

        MvcResult validRequestResult = mockMvc.perform(get("/shop/sell/{shopId} ", shopId)
                .session((MockHttpSession) session)
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.schedules[0].week").value(0))
                .andExpect(jsonPath("$.schedules[0].timePeriods[0].startTime").value("00:00"))
                .andExpect(jsonPath("$.schedules[0].timePeriods[0].endTime").value("00:00"))
                .andExpect(jsonPath("$.schedules[1].week").value(1))
                .andExpect(jsonPath("$.schedules[1].timePeriods[0].startTime").value("00:00"))
                .andExpect(jsonPath("$.schedules[1].timePeriods[0].endTime").value("00:00"))
                .andExpect(jsonPath("$.schedules[2].week").value(2))
                .andExpect(jsonPath("$.schedules[2].timePeriods[0].startTime").value("00:00"))
                .andExpect(jsonPath("$.schedules[2].timePeriods[0].endTime").value("00:00"))
                .andExpect(jsonPath("$.schedules[3].week").value(3))
                .andExpect(jsonPath("$.schedules[3].timePeriods[0].startTime").value("00:00"))
                .andExpect(jsonPath("$.schedules[3].timePeriods[0].endTime").value("00:00"))
                .andExpect(jsonPath("$.schedules[4].week").value(4))
                .andExpect(jsonPath("$.schedules[4].timePeriods[0].startTime").value("00:00"))
                .andExpect(jsonPath("$.schedules[4].timePeriods[0].endTime").value("00:00"))
                .andExpect(jsonPath("$.schedules[5].week").value(5))
                .andExpect(jsonPath("$.schedules[5].timePeriods[0].startTime").value("00:00"))
                .andExpect(jsonPath("$.schedules[5].timePeriods[0].endTime").value("00:00"))
                .andExpect(jsonPath("$.schedules[6].week").value(6))
                .andExpect(jsonPath("$.schedules[6].timePeriods[0].startTime").value("00:00"))
                .andExpect(jsonPath("$.schedules[6].timePeriods[0].endTime").value("00:00"))
                .andReturn();
        String validResponseContent = validRequestResult.getResponse().getContentAsString();
        System.out.println("validRequestResult: " + validRequestResult);
        System.out.println("validResponseContent: " + validResponseContent);

    }

    @Test
    @Rollback
    @Transactional
    void testPutSchedule_0_24() throws Exception {

        JSONArray scheduleWeeks = new JSONArray();
        for (int week = 0; week <= 6; week++) {
            JSONArray timePeriods = new JSONArray();

            JSONObject scheduleTime1 = new JSONObject();
            scheduleTime1.put("startTime", "00:00");
            scheduleTime1.put("endTime", "08:40");
            timePeriods.put(scheduleTime1);

            JSONObject scheduleTime2 = new JSONObject();
            scheduleTime2.put("startTime", "12:20");
            scheduleTime2.put("endTime", "15:20");
            timePeriods.put(scheduleTime2);

            JSONObject scheduleTime3 = new JSONObject();
            scheduleTime3.put("startTime", "18:30");
            scheduleTime3.put("endTime", "24:00");
            timePeriods.put(scheduleTime3);

            JSONObject scheduleWeek = new JSONObject();
            scheduleWeek.put("week", week);
            scheduleWeek.put("timePeriods", timePeriods);

            scheduleWeeks.put(scheduleWeek);
        }

        JSONObject schedulesJson = new JSONObject();
        schedulesJson.put("type", 0);
        schedulesJson.put("schedules", scheduleWeeks);
        int shopId = 3;

        mockMvc.perform(put("/schedule/{shopId}", shopId)
                .session((MockHttpSession) session)
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(schedulesJson.toString()))
                .andExpect(status().isOk())
                .andReturn();

        MvcResult validRequestResult = mockMvc.perform(get("/shop/sell/{shopId} ", shopId)
                .session((MockHttpSession) session)
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.schedules[0].week").value(0))
                .andExpect(jsonPath("$.schedules[0].timePeriods[0].startTime").value("00:00"))
                .andExpect(jsonPath("$.schedules[0].timePeriods[0].endTime").value("08:40"))
                .andExpect(jsonPath("$.schedules[0].timePeriods[1].startTime").value("12:20"))
                .andExpect(jsonPath("$.schedules[0].timePeriods[1].endTime").value("15:20"))
                .andExpect(jsonPath("$.schedules[0].timePeriods[2].startTime").value("18:30"))
                .andExpect(jsonPath("$.schedules[0].timePeriods[2].endTime").value("00:00"))
                .andExpect(jsonPath("$.schedules[1].week").value(1))
                .andExpect(jsonPath("$.schedules[1].timePeriods[0].startTime").value("00:00"))
                .andExpect(jsonPath("$.schedules[1].timePeriods[0].endTime").value("08:40"))
                .andExpect(jsonPath("$.schedules[1].timePeriods[1].startTime").value("12:20"))
                .andExpect(jsonPath("$.schedules[1].timePeriods[1].endTime").value("15:20"))
                .andExpect(jsonPath("$.schedules[1].timePeriods[2].startTime").value("18:30"))
                .andExpect(jsonPath("$.schedules[1].timePeriods[2].endTime").value("00:00"))
                .andExpect(jsonPath("$.schedules[2].week").value(2))
                .andExpect(jsonPath("$.schedules[2].timePeriods[0].startTime").value("00:00"))
                .andExpect(jsonPath("$.schedules[2].timePeriods[0].endTime").value("08:40"))
                .andExpect(jsonPath("$.schedules[2].timePeriods[1].startTime").value("12:20"))
                .andExpect(jsonPath("$.schedules[2].timePeriods[1].endTime").value("15:20"))
                .andExpect(jsonPath("$.schedules[2].timePeriods[2].startTime").value("18:30"))
                .andExpect(jsonPath("$.schedules[2].timePeriods[2].endTime").value("00:00"))
                .andExpect(jsonPath("$.schedules[3].week").value(3))
                .andExpect(jsonPath("$.schedules[3].timePeriods[0].startTime").value("00:00"))
                .andExpect(jsonPath("$.schedules[3].timePeriods[0].endTime").value("08:40"))
                .andExpect(jsonPath("$.schedules[3].timePeriods[1].startTime").value("12:20"))
                .andExpect(jsonPath("$.schedules[3].timePeriods[1].endTime").value("15:20"))
                .andExpect(jsonPath("$.schedules[3].timePeriods[2].startTime").value("18:30"))
                .andExpect(jsonPath("$.schedules[3].timePeriods[2].endTime").value("00:00"))
                .andExpect(jsonPath("$.schedules[4].week").value(4))
                .andExpect(jsonPath("$.schedules[4].timePeriods[0].startTime").value("00:00"))
                .andExpect(jsonPath("$.schedules[4].timePeriods[0].endTime").value("08:40"))
                .andExpect(jsonPath("$.schedules[4].timePeriods[1].startTime").value("12:20"))
                .andExpect(jsonPath("$.schedules[4].timePeriods[1].endTime").value("15:20"))
                .andExpect(jsonPath("$.schedules[4].timePeriods[2].startTime").value("18:30"))
                .andExpect(jsonPath("$.schedules[4].timePeriods[2].endTime").value("00:00"))
                .andExpect(jsonPath("$.schedules[5].week").value(5))
                .andExpect(jsonPath("$.schedules[5].timePeriods[0].startTime").value("00:00"))
                .andExpect(jsonPath("$.schedules[5].timePeriods[0].endTime").value("08:40"))
                .andExpect(jsonPath("$.schedules[5].timePeriods[1].startTime").value("12:20"))
                .andExpect(jsonPath("$.schedules[5].timePeriods[1].endTime").value("15:20"))
                .andExpect(jsonPath("$.schedules[5].timePeriods[2].startTime").value("18:30"))
                .andExpect(jsonPath("$.schedules[5].timePeriods[2].endTime").value("00:00"))
                .andExpect(jsonPath("$.schedules[6].week").value(6))
                .andExpect(jsonPath("$.schedules[6].timePeriods[0].startTime").value("00:00"))
                .andExpect(jsonPath("$.schedules[6].timePeriods[0].endTime").value("08:40"))
                .andExpect(jsonPath("$.schedules[6].timePeriods[1].startTime").value("12:20"))
                .andExpect(jsonPath("$.schedules[6].timePeriods[1].endTime").value("15:20"))
                .andExpect(jsonPath("$.schedules[6].timePeriods[2].startTime").value("18:30"))
                .andExpect(jsonPath("$.schedules[6].timePeriods[2].endTime").value("00:00"))
                .andReturn();
        String validResponseContent = validRequestResult.getResponse().getContentAsString();
        System.out.println("validRequestResult: " + validRequestResult);
        System.out.println("validResponseContent: " + validResponseContent);

    }

    @Test
    @Rollback
    @Transactional
    void testPutSchedule_Valid_SchedulesRequest() throws Exception {

        List<Integer> arrayList = new ArrayList<Integer>();
        arrayList.add(null);
        arrayList.add(-1);
        arrayList.add(2);

        for (int i = 0; i < arrayList.size(); i++) {
            JSONArray scheduleWeeks = new JSONArray();
            for (int week = 0; week <= 6; week++) {
                JSONArray timePeriods = new JSONArray();

                JSONObject scheduleTime1 = new JSONObject();
                scheduleTime1.put("startTime", "00:00");
                scheduleTime1.put("endTime", "08:40");
                timePeriods.put(scheduleTime1);

                JSONObject scheduleTime2 = new JSONObject();
                scheduleTime2.put("startTime", "12:20");
                scheduleTime2.put("endTime", "15:20");
                timePeriods.put(scheduleTime2);

                JSONObject scheduleTime3 = new JSONObject();
                scheduleTime3.put("startTime", "18:30");
                scheduleTime3.put("endTime", "24:00");
                timePeriods.put(scheduleTime3);

                JSONObject scheduleWeek = new JSONObject();
                scheduleWeek.put("week", week);
                scheduleWeek.put("timePeriods", timePeriods);

                scheduleWeeks.put(scheduleWeek);
            }

            JSONObject schedulesJson = new JSONObject();
            schedulesJson.put("type", arrayList.get(i));
            schedulesJson.put("schedules", null);
            int shopId = 3;

            MvcResult validRequestResult = mockMvc.perform(put("/schedule/{shopId}", shopId)
                    .session((MockHttpSession) session)
                    .with(SecurityMockMvcRequestPostProcessors.csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(schedulesJson.toString()))
                    .andExpect(status().isBadRequest())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.type").value(Matchers.anyOf(
                            Matchers.is("must not be null"),
                            Matchers.is("must be greater than or equal to 0"),
                            Matchers.is("must be less than or equal to 1"))))
                    .andExpect(jsonPath("$.scheduleWeeks", is("must not be null")))
                    .andReturn();

            String validResponseContent = validRequestResult.getResponse().getContentAsString();
            System.out.println("validRequestResult: " + validRequestResult);
            System.out.println("validResponseContent: " + validResponseContent);
        }
    }

    @Test
    @Rollback
    @Transactional
    void testPutSchedule_Valid_ScheduleWeek() throws Exception {

        JSONArray scheduleWeeks = new JSONArray();
        for (int week = 0; week <= 6; week++) {
            JSONArray timePeriods = new JSONArray();

            JSONObject scheduleTime1 = new JSONObject();
            scheduleTime1.put("startTime", "00:00");
            scheduleTime1.put("endTime", "08:40");
            timePeriods.put(scheduleTime1);

            JSONObject scheduleTime2 = new JSONObject();
            scheduleTime2.put("startTime", "12:20");
            scheduleTime2.put("endTime", "15:20");
            timePeriods.put(scheduleTime2);

            JSONObject scheduleTime3 = new JSONObject();
            scheduleTime3.put("startTime", "18:30");
            scheduleTime3.put("endTime", "24:00");
            timePeriods.put(scheduleTime3);

            JSONObject scheduleWeek = new JSONObject();

            switch (week) {
                case 0:
                    scheduleWeek.put("week", -1);
                    break;
                case 5:
                    scheduleWeek.put("week", null);
                    break;
                case 6:
                    scheduleWeek.put("week", 7);
                    break;
                default:
                    scheduleWeek.put("week", week);
            }

            scheduleWeek.put("timePeriods", timePeriods);

            scheduleWeeks.put(scheduleWeek);
        }

        JSONObject schedulesJson = new JSONObject();
        schedulesJson.put("type", 0);
        schedulesJson.put("schedules", scheduleWeeks);
        int shopId = 3;

        MvcResult validRequestResult = mockMvc.perform(put("/schedule/{shopId}", shopId)
                .session((MockHttpSession) session)
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(schedulesJson.toString()))
                .andExpect(status().isBadRequest())
                // 注意scheduleWeeks[6].week回傳的是物件要用['']包括起來才不會便陣列
                .andExpect(jsonPath("$.['scheduleWeeks[6].week']", is("must be less than or equal to 6")))
                .andExpect(jsonPath("$.['scheduleWeeks[0].week']", is("must be greater than or equal to 0")))
                .andExpect(jsonPath("$.['scheduleWeeks[5].week']", is("must not be null")))
                .andReturn();

        String validResponseContent = validRequestResult.getResponse().getContentAsString();
        System.out.println("validRequestResult: " + validRequestResult);
        System.out.println("validResponseContent: " + validResponseContent);
    }


    //以後有機會在改
    @Test
    @Rollback
    @Transactional
    void testPutSchedule_Valid_TimePeriod() throws Exception {

        JSONArray scheduleWeeks = new JSONArray();
        for (int week = 0; week <= 6; week++) {
            JSONArray timePeriods = new JSONArray();

            JSONObject scheduleTime1 = new JSONObject();
            scheduleTime1.put("startTime", "08:30");
            scheduleTime1.put("endTime", "08:40");
            timePeriods.put(scheduleTime1);

            JSONObject scheduleTime2 = new JSONObject();
            scheduleTime2.put("startTime", "12:20");
            scheduleTime2.put("endTime", "15:20");
            timePeriods.put(scheduleTime2);

            JSONObject scheduleTime3 = new JSONObject();
            scheduleTime3.put("startTime", "18:30");
            scheduleTime3.put("endTime", "25:00");
            timePeriods.put(scheduleTime3);

            JSONObject scheduleWeek = new JSONObject();

            scheduleWeek.put("week", week);
            scheduleWeek.put("timePeriods", timePeriods);

            scheduleWeeks.put(scheduleWeek);
        }

        JSONObject schedulesJson = new JSONObject();
        schedulesJson.put("type", 0);
        schedulesJson.put("schedules", scheduleWeeks);
        int shopId = 3;

        MvcResult validRequestResult = mockMvc.perform(put("/schedule/{shopId}", shopId)
                .session((MockHttpSession) session)
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(schedulesJson.toString()))
                .andExpect(status().isBadRequest())
                // 注意scheduleWeeks[6].week回傳的是物件要用['']包括起來才不會便陣列
                // .andExpect(jsonPath("$.['scheduleWeeks[6].week']", is("must be less than or
                // equal to 6")))
                // .andExpect(jsonPath("$.['scheduleWeeks[0].week']", is("must be greater than
                // or equal to 0")))
                // .andExpect(jsonPath("$.['scheduleWeeks[5].week']", is("must not be null")))
                .andReturn();

        String validResponseContent = validRequestResult.getResponse().getContentAsString();
        System.out.println("validRequestResult: " + validRequestResult);
        System.out.println("validResponseContent: " + validResponseContent);
    }
}
