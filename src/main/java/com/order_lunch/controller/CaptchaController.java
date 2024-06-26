package com.order_lunch.controller;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;

// import org.apache.tomcat.jni.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.order_lunch.service.Impl.ShopService;
import com.order_lunch.service.Impl.UserService;

@Validated
@RestController
@RequestMapping("/register")
public class CaptchaController {

    @Autowired
    UserService userService;

    @Autowired
    ShopService shopService;



    @GetMapping("/captcha")
    @ResponseBody
    public byte[] generateCaptcha(HttpSession session, @RequestParam String timestamp) throws IOException {
        int width = 130;
        int height = 60;

        BufferedImage captchaImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = captchaImage.createGraphics();

        // 填充背景色
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, width, height);

        // 生成隨機字符
        String captchaText = generateRandomString();
        graphics.setColor(Color.BLACK);
        Font font = new Font("Arial", Font.BOLD, 40);
        graphics.setFont(font);
        graphics.drawString(captchaText, 10, 45);

        session.setAttribute("captchaText", captchaText);

        // 啟動定時器，在5分鐘後刪除Session中的驗證碼
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                session.removeAttribute("captcha");
                timer.cancel(); // 取消定時器
            }
        }, 5 * 60 * 1000); // 5分鐘後執行

        System.out.println("captchaText:" + captchaText);
        applyBlur(captchaImage);

        // 生成隨機干擾線
        drawInterferenceLines(graphics);

        // 釋放資源
        graphics.dispose();

        // 将验证码图片的 byte 数组作为响应体返回
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(captchaImage, "png", baos);
        return baos.toByteArray();
    }

    @PostMapping("/verifyCaptcha")
    public String verifyCaptcha(@RequestBody String captchaText, HttpSession session) {
        // String userInput = requestBody.get("userInput");
        String storedCaptcha = (String) session.getAttribute("captchaText");

        if (storedCaptcha != null && storedCaptcha.equals(captchaText)) {
            // 驗證通過
            session.removeAttribute("captchaText"); // 驗證成功後從Session中移除
            return "Verification successful!";
        } else {
            // 驗證失敗
            return "Verification failed. Invalid captcha.";
        }
    }

    private void drawInterferenceLines(Graphics2D graphics) {
        Random random = new Random();

        for (int i = 0; i < 20; i++) {
            int x1 = random.nextInt(130);
            int y1 = random.nextInt(60);
            int x2 = random.nextInt(130);
            int y2 = random.nextInt(60);

            graphics.setColor(getRandomColor());
            graphics.drawLine(x1, y1, x2, y2);
        }
    }

    private Color getRandomColor() {
        Random random = new Random();
        int red = random.nextInt(256);
        int green = random.nextInt(256);
        int blue = random.nextInt(256);
        return new Color(red, green, blue);
    }

    private String generateRandomString() {
        // 生成包含字母和數字的隨機字串
        // String characters =
        // "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        // String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        String characters = "0123456789";
        // String characters = "abcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder captchaText = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 4; i++) {
            captchaText.append(characters.charAt(random.nextInt(characters.length())));
        }
        return captchaText.toString();
    }

    private void applyBlur(BufferedImage image) {
        int size = 5; // 模糊核心的大小，可以根據需要調整

        float[] blurMatrix = new float[size * size];
        for (int i = 0; i < size * size; i++) {
            blurMatrix[i] = 1.0f / (size * size);
        }

        BufferedImageOp blurFilter = new ConvolveOp(new Kernel(size, size, blurMatrix));
        BufferedImage blurredImage = blurFilter.filter(image, null);

        // 將模糊後的圖片複製回原始圖片
        Graphics2D graphics = image.createGraphics();
        graphics.drawImage(blurredImage, 0, 0, null);
        graphics.dispose();
    }




}
