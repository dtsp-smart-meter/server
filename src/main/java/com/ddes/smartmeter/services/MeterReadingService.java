package com.ddes.smartmeter.services;

import com.ddes.smartmeter.entities.ListenerDetails;
import com.ddes.smartmeter.entities.MeterReading;
import com.ddes.smartmeter.websockets.WebSocketResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class MeterReadingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MeterReadingService.class);

    private static final double COST_PER_KWH = 0.12;
    // will need to be updated to use external API

    private double calculateCost(MeterReading reading) {

        double currentUsage = reading.getCurrentUsage();
        UUID clientID = reading.getClientId();
        long timestamp = reading.getTimestamp();

        double calcutatedPrice = currentUsage * COST_PER_KWH;

        LOGGER.info("Calculated electricity cost for Client Id: " + clientID + " is £" + calcutatedPrice);

        return calcutatedPrice;
    }

    public String processedMeterReading(ListenerDetails listener, MeterReading reading) {
        double calculatedCost = calculateCost(reading);

        ObjectMapper mapper = new ObjectMapper();

        ObjectNode jsonObject = mapper.createObjectNode();

        jsonObject.put("clientId", reading.getClientId().toString());
        jsonObject.put("timestamp", reading.getTimestamp());
        jsonObject.put("currentUsage", reading.getCurrentUsage());
        jsonObject.put("cost", calculatedCost);

        try {
            String jsonString = mapper.writeValueAsString(jsonObject);
            LOGGER.info("JSON created for reading: " + jsonString);
            return jsonString;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
 }