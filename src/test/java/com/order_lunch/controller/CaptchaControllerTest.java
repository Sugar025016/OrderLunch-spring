package com.order_lunch.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


@SpringBootTest
@AutoConfigureMockMvc // 自動配置 MockMvc
@ActiveProfiles("test") // 加載 application-test.properties 文件配置
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CaptchaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGenerateCaptcha() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/register/captcha")
                        .param("timestamp", "1234567890"))
                .andExpect(status().isOk())
                .andReturn();

        HttpSession session = mvcResult.getRequest().getSession();
        @SuppressWarnings("null")//取消session 空值警告
        String captchaText = (String) session.getAttribute("captchaText");
        assertThat(captchaText).isNotNull();

        // 检查响应的图片数据
        byte[] captchaImageBytes = mvcResult.getResponse().getContentAsByteArray();
        assertThat(captchaImageBytes).isNotEmpty();

        // 验证生成的图像内容
        BufferedImage captchaImage = ImageIO.read(new ByteArrayInputStream(captchaImageBytes));
        assertThat(captchaImage).isNotNull();
        assertThat(captchaImage.getWidth()).isEqualTo(130);
        assertThat(captchaImage.getHeight()).isEqualTo(60);
    }
}
