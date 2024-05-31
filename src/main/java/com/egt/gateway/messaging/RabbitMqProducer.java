package com.egt.gateway.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class RabbitMqProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendMessage(String queueName, String message) {
        try{
            rabbitTemplate.convertAndSend(queueName, message);
        }catch (Exception e){
            log.error("MQ is not configured. {}",e.getMessage());
        }
    }
}
