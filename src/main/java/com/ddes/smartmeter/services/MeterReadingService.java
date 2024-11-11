package com.ddes.smartmeter.services;

import com.ddes.smartmeter.entities.MeterReading;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class MeterReadingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MeterReadingService.class);
    private static final double COST_PER_KWH = 0.12;

    private double calculateCost(MeterReading reading) {
        double currentUsage = reading.getCurrentUsage();
        UUID clientID = reading.getClientId();
        long timestamp = reading.getTimestamp();

        double calcutatedPrice = currentUsage * COST_PER_KWH;

        LOGGER.info("Calculated electricity cost for Client Id: " + clientID + " is £" + calcutatedPrice);

        return calcutatedPrice;
    }

    public String processedMeterReading(MeterReading meterReading) {
        if (meterReading == null) {
            LOGGER.error("Meter reading cannot be null.");
            throw new NullPointerException("MeterReading cannot be null.");
        }

        if (meterReading.getCurrentUsage() < 0) {
            LOGGER.error("Current usage cannot be negative for reading: " + meterReading);
            throw new IllegalArgumentException("Current usage cannot be negative.");
        }

        double calculatedCost = calculateCost(meterReading);

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode jsonObject = mapper.createObjectNode();
        jsonObject.put("clientId", meterReading.getClientId().toString());
        jsonObject.put("timestamp", meterReading.getTimestamp());
        jsonObject.put("currentUsage", meterReading.getCurrentUsage());
        jsonObject.put("cost", calculatedCost);

        try {
            String jsonString = mapper.writeValueAsString(jsonObject);

            LOGGER.info("JSON created for reading: " + jsonString);

            return jsonString;
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }
 }