package com.ddes.smartmeter.rabbit;

import com.ddes.smartmeter.services.NotificationDispatcherService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitReceiver {

    @Autowired
    private NotificationDispatcherService notificationDispatcher;

    @RabbitListener(queues = "meterReadings")
    public void receiveMessage(String message) {

        try {
            System.out.println("Received meter reading: " + message);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(message);
            String clientId = rootNode.get("clientId").asText();

            notificationDispatcher.getListeners().stream()
                    .filter(listener -> listener.getClientId().equals(clientId))
                    .forEach(listener -> notificationDispatcher.dispatchMeterReading(listener.getSessionId(), message));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}