package com.ddes.smartmeter.websockets;

import com.ddes.smartmeter.entities.MeterReading;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public WebSocketController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @MessageMapping("/meterReading")
    public void meterReadingResponse(MeterReading meterReading, @Header("clientId") String clientId) throws Exception {
        // Set clientId from header
        meterReading.setClientId(clientId);

        rabbitTemplate.convertAndSend("meterReadings", meterReading.toJSON());
    }
}