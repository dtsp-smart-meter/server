package com.ddes.smartmeter.websockets;

import com.ddes.smartmeter.entities.MeterReading;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public WebSocketController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    // webSocket endpoint for meter reading messages
    @MessageMapping("/meterReading")
    @SendTo("/topic/smartMeter")
    public WebSocketResponse meterReadingResponse(MeterReading meterReading) throws Exception {
        rabbitTemplate.convertAndSend("meterReadings", meterReading.toJSON());

        return new WebSocketResponse("Meter reading recieved. Await response from RabbitMQ...");
    }
}