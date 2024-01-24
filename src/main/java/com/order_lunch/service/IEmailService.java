package com.order_lunch.service;

public interface IEmailService {

    public void sendSimpleMessage(
            String to, String subject, String text);
}
