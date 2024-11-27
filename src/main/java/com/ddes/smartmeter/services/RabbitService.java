package com.ddes.smartmeter.services;

import com.ddes.smartmeter.entities.MeterReading;
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

/**
 * Service class for handling incoming messages from the RabbitMQ queue.
 * This class listens for meter readings, processes them, calculates the total bill for each client,
 * and sends the result back to clients via the NotificationDispatcherService.
 */
@Service
public class RabbitService {

    // Logger instance for logging messages and errors.
    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitService.class);

    // Service used to dispatch notifications to clients.
    @Autowired
    private NotificationDispatcherService notificationDispatcher;

    // Mocking map that stores the total bill for each client.
    private final Map<UUID, Double> clientTotalBills = new HashMap<>();

    /**
     * Listener method that is triggered when a message is received from the RabbitMQ queue 'meterReadings'.
     * This method processes the incoming meter reading data, calculates the total bill for the client,
     * and sends the result to the client.
     */
    @RabbitListener(queues = "meterReadings")
    public void receiveMessage(String message) {
        // Log the received message for debugging and monitoring purposes.
        LOGGER.info("Received message: " + message);

        try {
            // Parse the received message into a JsonNode for further processing.
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(message);

            // Create a MeterReading object from the parsed JSON data.
            MeterReading meterReading = new MeterReading(
                    rootNode.get("clientId").asText(),
                    rootNode.get("currentUsage").asDouble(),
                    rootNode.get("timestamp").asLong());

            // Calculate the current cost for the client based on the usage.
            double currentCost = meterReading.getCurrentCost();

            // Update the total bill for the client by adding the current cost.
            clientTotalBills.put(meterReading.getClientId(), clientTotalBills.getOrDefault(meterReading.getClientId(), 0.0) + currentCost);

            // Retrieve the total bill for the client.
            double totalBill = clientTotalBills.get(meterReading.getClientId());

            // Create the result JSON string to be sent to the client.
            String jsonString = createJsonString(meterReading, totalBill);

            // Dispatch the result notification to the specific client via WebSocket.
            notificationDispatcher.dispatchNotification("readingResult/" + meterReading.getClientId().toString(), jsonString);
        } catch (Exception exception) {
            // Log any exception that occurs during message processing.
            exception.printStackTrace();
        }
    }

    /**
     * Creates a JSON string representation of the meter reading and its associated total bill.
     */
    public String createJsonString(MeterReading meterReading, double totalBill) throws JsonProcessingException {
        // Create an ObjectMapper instance to handle JSON creation.
        ObjectMapper mapper = new ObjectMapper();

        // Create an ObjectNode to represent the JSON structure.
        ObjectNode jsonObject = mapper.createObjectNode();
        jsonObject.put("clientId", meterReading.getClientId().toString());
        jsonObject.put("currentUsage", meterReading.getCurrentUsage());
        jsonObject.put("currentCost", meterReading.getCurrentCost());
        jsonObject.put("totalBill", totalBill);
        jsonObject.put("timestamp", meterReading.getTimestamp());

        // Convert the ObjectNode to a JSON string and return it.
        return mapper.writeValueAsString(jsonObject);
    }
}