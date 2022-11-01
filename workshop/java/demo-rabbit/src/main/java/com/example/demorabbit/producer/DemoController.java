package com.example.demorabbit.producer;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/demo/{name}")
    public String sendData(@PathVariable String name) {
        rabbitTemplate.convertAndSend(name);
        return "OK";
    }

}
