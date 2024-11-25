package com.ddes.smartmeter.rabbit;

import com.ddes.smartmeter.entities.MeterReading;
import com.ddes.smartmeter.services.NotificationDispatcherService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class RabbitReceiver {

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitReceiver.class);

    @Autowired
    private NotificationDispatcherService notificationDispatcher;

    private final Map<UUID, Double> clientTotalBills = new HashMap<>();

    @RabbitListener(queues = "meterReadings")
    public void receiveMessage(String message) {
        LOGGER.info("Received message: " + message);

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(message);

            MeterReading meterReading = new MeterReading(
                    rootNode.get("clientId").asText(),
                    rootNode.get("currentUsage").asDouble(),
                    rootNode.get("timestamp").asLong());

            double currentCost = meterReading.getCurrentCost();

            clientTotalBills.put(meterReading.getClientId(), clientTotalBills.getOrDefault(meterReading.getClientId(), 0.0) + currentCost);

            double totalBill = clientTotalBills.get(meterReading.getClientId());

            String jsonString = createJsonString(meterReading, totalBill);

            notificationDispatcher.dispatchNotification("readingResult/" + meterReading.getClientId().toString(), jsonString);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public String createJsonString(MeterReading meterReading, double totalBill) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode jsonObject = mapper.createObjectNode();
        jsonObject.put("clientId", meterReading.getClientId().toString());
        jsonObject.put("currentUsage", meterReading.getCurrentUsage());
        jsonObject.put("currentCost", meterReading.getCurrentCost());
        jsonObject.put("totalBill", totalBill);
        jsonObject.put("timestamp", meterReading.getTimestamp());

        return mapper.writeValueAsString(jsonObject);
    }
}