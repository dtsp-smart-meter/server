package com.ddes.smartmeter.controllers;

import com.ddes.smartmeter.entities.MeterReading;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

/**
 * Controller for handling WebSocket messages related to meter readings.
 * This controller processes incoming WebSocket messages and forwards them to RabbitMQ for further processing.
 */
@Controller
public class MeterReadingController {

    // RabbitTemplate for sending messages to RabbitMQ.
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public MeterReadingController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * Handles WebSocket messages sent to the "/meterReading" endpoint.
     * The method processes the incoming meter reading, associates it with a client ID, and sends it to RabbitMQ.
     */
    @MessageMapping("/meterReading")
    public void meterReadingResponse(MeterReading meterReading, @Header("clientId") String clientId) throws Exception {
        // Associate the meter reading with the client ID from the WebSocket message header.
        meterReading.setClientId(clientId);

        // Convert the meter reading to JSON and send it to the "meterReadings" queue in RabbitMQ.
        rabbitTemplate.convertAndSend("meterReadings", meterReading.toJSON());
    }
}