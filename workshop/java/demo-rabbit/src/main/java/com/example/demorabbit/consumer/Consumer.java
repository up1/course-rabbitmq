package com.example.demorabbit.consumer;

import com.example.demorabbit.RabbitConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class Consumer {

    @RabbitListener(queues = RabbitConfig.Q_HELLO)
    public void onMessage(String message) {
        System.out.println("Received message : "+ message);
    }

}
