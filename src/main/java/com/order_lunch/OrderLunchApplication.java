package com.order_lunch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import com.order_lunch.config.AppConfig;

@SpringBootApplication
@Import(AppConfig.class)
public class OrderLunchApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderLunchApplication.class, args);
	}

}
