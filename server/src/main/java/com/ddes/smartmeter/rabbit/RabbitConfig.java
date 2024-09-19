package com.ddes.smartmeter.rabbit;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Bean
    public Queue meterReadingsQueue() {
        return new Queue("meterReadings", false);
    }
}