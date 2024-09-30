package com.ddes.smartmeter.rabbit;

import com.ddes.smartmeter.services.NotificationDispatcherService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitReceiver {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationDispatcherService.class);

    @Autowired
    private NotificationDispatcherService notificationDispatcher;

    @RabbitListener(queues = "meterReadings")
    public void receiveMessage(String message) {
        LOGGER.info("Recieved message: " + message);

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(message);

            String clientId = rootNode.get("clientId").asText();

            // Write logic code here...

            notificationDispatcher.dispatchMeterReading(clientId, message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}