package com.example.springboot.rabbitMQ.consumer;

import com.example.springboot.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class ProductConsumer {

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void listen(String message){
        System.out.println("Received message" + message);
    }
}
