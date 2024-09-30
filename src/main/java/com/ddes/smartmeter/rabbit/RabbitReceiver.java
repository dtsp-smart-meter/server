package com.ddes.smartmeter.rabbit;

import com.ddes.smartmeter.entities.MeterReading;
import com.ddes.smartmeter.services.MeterReadingService;
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
    @Autowired
    private MeterReadingService meterReadingService;

    @RabbitListener(queues = "meterReadings")
    public void receiveMessage(String message) {
        LOGGER.info("Recieved message: " + message);

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(message);

            String clientId = rootNode.get("clientId").asText();
            double currentUsage = rootNode.get("currentUsage").asDouble();
            long timestamp = rootNode.get("timestamp").asLong();

            MeterReading reading = new MeterReading(clientId, currentUsage, timestamp);

            String processedMessage= meterReadingService.processedMeterReading(reading);

            notificationDispatcher.dispatchMeterReading(clientId, processedMessage);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}