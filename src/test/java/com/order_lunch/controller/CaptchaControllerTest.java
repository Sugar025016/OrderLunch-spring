package com.order_lunch.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


@SpringBootTest
@AutoConfigureMockMvc
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
