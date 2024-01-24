package com.order_lunch.service.Impl;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.order_lunch.service.IEmailService;

@Service
@Transactional
public class EmailService implements IEmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Async
    public void sendSimpleMessage(
            String to, String subject, String text) {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        text = "<html lang='en'>"+
        "<head>"+
        "<meta charset='UTF-8'>"+
        "<meta http-equiv='X-UA-Compatible' content='IE=edge'>"+
        "<meta name='viewport' content='width=device-width, initial-scale=1.0'>"+
        "<title>Email Verification</title>"+
        "</head>"+
        
        "<body style='font-family: Arial, sans-serif; text-align: center; background-color: #f4f4f4; padding: 20px;'>"+

        "<h1 style='color: #fd7e14;'>餓餓定便當（電子郵件）驗證</h1>"+

        "<p>謝謝您註冊！如需完成註冊，請點選以下連結：</p>"+

        "<a href='YOUR_VERIFICATION_ENDPOINT' style='display: inline-block; padding:10px 20px; background-color: #3498db; color: #ffffff; text-decoration: none; border-radius: 5px;'>驗證郵件</a>"+

        "<p>如果您沒有註冊，您可以放心地忽略這封電子郵件。</p>"+

        "<p>此致，<br>餓餓定便當團隊敬上</p>"+

        "</body>"+
        "</html>";


        System.out.println("業務進行中....");
        // SimpleMailMessage message = new SimpleMailMessage();
        // message.setFrom("sugar025016@gmail.com");
        // message.setTo(to);
        // message.setSubject(subject);
        // message.setContent(text, "text/html; charset=utf-8");
        // // message.setText(text);
        // javaMailSender.send(message);

        // MimeMessage message = new MimeMessage(Email.getSession());
        // message.setFrom(new InternetAddress(to));
        // message.addRecipient(Message.RecipientType.TO, new InternetAddress(to,
        // true));
        // message.setSubject(subject);
        // message.setContent(text, "text/html; charset=utf-8");
        // message.setText(text);
        // message.saveChanges();
        // Transport.send(message);

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper;
        try {
            helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, true); // 第二個參數設置為 true，表示使用 HTML 格式

            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
