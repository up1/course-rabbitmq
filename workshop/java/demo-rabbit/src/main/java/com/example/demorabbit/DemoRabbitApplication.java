package com.example.demorabbit;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableRabbit
public class DemoRabbitApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoRabbitApplication.class, args);
	}

}
