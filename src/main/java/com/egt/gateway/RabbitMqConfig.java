package com.egt.gateway;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.egt.gateway.Constants.EXCHANGE_RATE_QUEUE_NAME;
import static com.egt.gateway.Constants.REQUEST_LOG_QUEUE_NAME;

@Configuration
public class RabbitMqConfig {

    @Bean
    public Queue exchangeRateQueue(){
        return new Queue(EXCHANGE_RATE_QUEUE_NAME, true);
    }

    @Bean
    public Queue requestLogQueue(){
        return new Queue(REQUEST_LOG_QUEUE_NAME, true);
    }
}
