package com.ddes.smartmeter.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for setting up RabbitMQ queues.
 * This class defines beans that create and configure RabbitMQ queues for the application.
 */
@Configuration
public class RabbitConfig {

    @Bean
    public Queue meterReadingsQueue() {
        return new Queue("meterReadings", false);
    }
}