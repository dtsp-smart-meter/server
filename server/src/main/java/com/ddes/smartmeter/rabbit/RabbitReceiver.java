package com.ddes.smartmeter.rabbit;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class RabbitReceiver {

    @RabbitListener(queues = "meterReadings")
    public void receiveMessage(String message) {
        try {
            System.out.println("Received meter reading: " + message);
            // Process the meterReading object here
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}