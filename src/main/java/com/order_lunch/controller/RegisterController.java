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
import javax.validation.Valid;

// import org.apache.tomcat.jni.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.order_lunch.config.CustomUserDetails;
import com.order_lunch.entity.Shop;
import com.order_lunch.entity.User;
import com.order_lunch.enums.NewErrorStatus;
import com.order_lunch.model.ErrorResponse;
import com.order_lunch.model.request.ShopRequest;
import com.order_lunch.model.request.UserRequest;
import com.order_lunch.service.Impl.ShopService;
import com.order_lunch.service.Impl.UserService;

@Validated
@RestController
@RequestMapping("/api/register")
public class RegisterController {

    @Autowired
    UserService userService;

    @Autowired
    ShopService shopService;

    @RequestMapping(value = "/member", method = RequestMethod.POST)
    public ResponseEntity<?> addUser(HttpSession session,
            @RequestBody() @Valid UserRequest userRequest) {
        String storedCaptcha = (String) session.getAttribute("captchaText");

        if (storedCaptcha == null) {

            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setCode(NewErrorStatus.CAPTCHA_ERROR.getKey());
            errorResponse.setMessage(NewErrorStatus.CAPTCHA_ERROR.getChinese());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);

        }

        if (!storedCaptcha.equals(userRequest.getVerifyCode())) {

            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setCode(NewErrorStatus.CAPTCHA_ERROR.getKey());
            errorResponse.setMessage(NewErrorStatus.CAPTCHA_ERROR.getChinese());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);

        }

        if (userService.accountExists(userRequest.getAccount())) {
            ErrorResponse errorResponse = new ErrorResponse(NewErrorStatus.ACCOUNT_EXISTS.getKey(),NewErrorStatus.ACCOUNT_EXISTS.getChinese());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        session.removeAttribute("captchaText"); // 驗證成功後從Session中移除
        userService.addMember(userRequest);
        

        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/shop", method = RequestMethod.POST)
    public ResponseEntity<?> addShop(HttpSession session,
            @RequestBody() @Valid ShopRequest shopRequest,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        // Authentication authentication =
        // SecurityContextHolder.getContext().getAuthentication();

        ErrorResponse errorResponse = new ErrorResponse();
        if (customUserDetails == null) {
            // return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(validationError);
            // throw new IllegalArgumentException("customUserDetails cannot be null");
            // return ResponseEntity.badRequest().build();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            // return ResponseEntity.of();
        }

        String storedCaptcha = (String) session.getAttribute("captchaText");

        // 圖片驗證 暫時關閉
        if (storedCaptcha == null || !storedCaptcha.equals(shopRequest.getCaptcha())) {
            // validationError.addFieldError("captcha", Status.CAPTCHA_MISTAKE.getKey());
            // Response res = new Response();
            // res.setMessage(storedCaptcha);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("圖形驗證碼錯誤");
        }
        session.removeAttribute("captchaText"); // 驗證成功後從Session中移除

        if (shopService.existsByName(shopRequest.getName())) {
            errorResponse.setCode(NewErrorStatus.SHOP_DUPLICATE_NAME.getKey());
            errorResponse.setMessage(NewErrorStatus.SHOP_DUPLICATE_NAME.getChinese());
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(errorResponse);
        }
        User user = userService.findById(customUserDetails.getId());
        Shop shop = shopService.addShop(shopRequest, user);

        return ResponseEntity.ok().body(shop.getId());
    }

    // @RequestMapping(value = "/shop", method = RequestMethod.POST)
    // public ResponseEntity<Response> addShop(HttpSession session,
    // @RequestBody() @Valid ShopRequest shopRequest,@AuthenticationPrincipal
    // CustomUserDetails customUserDetails) {
    // // Authentication authentication =
    // SecurityContextHolder.getContext().getAuthentication();

    // Response response = new Response();
    // if (customUserDetails == null ) {
    // response.setCode(Status.OK);
    // return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
    // }
    // String storedCaptcha = (String) session.getAttribute("captchaText");

    // if (storedCaptcha == null || !storedCaptcha.equals(shopRequest.getCaptcha()))
    // {

    // return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
    // }
    // session.removeAttribute("captchaText"); // 驗證成功後從Session中移除
    // shopService.addShop(shopRequest);

    // return ResponseEntity.ok().build();
    // }

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
