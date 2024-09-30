package com.ddes.smartmeter.rabbit;

import com.ddes.smartmeter.entities.ListenerDetails;
import com.ddes.smartmeter.entities.MeterReading;
import com.ddes.smartmeter.services.MeterReadingService;
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
    @Autowired
    private MeterReadingService meterReadingService;

    @RabbitListener(queues = "meterReadings")
    public void receiveMessage(String message) {

        try {
            System.out.println("Received meter reading: " + message);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(message);
            String clientId = rootNode.get("clientId").asText();
            double currentUsage = rootNode.get("currentUsage").asDouble();
            long timestamp = rootNode.get("timestamp").asLong();

            MeterReading reading = new MeterReading(clientId, currentUsage, timestamp);

            ListenerDetails listener = notificationDispatcher.getListeners().stream()
                    .filter(l -> l.getClientId().equals(clientId))
                    .findFirst()
                    .orElse(null);

            System.out.print("ClientID held in rabbit Listener:" + listener.getClientId());

            notificationDispatcher.dispatchMeterReading(listener, message);

            meterReadingService.processedMeterReading(listener, reading);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}