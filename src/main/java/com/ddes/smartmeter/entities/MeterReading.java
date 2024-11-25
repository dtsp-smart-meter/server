package com.ddes.smartmeter.entities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.UUID;

public class MeterReading {

    private static final double COST_PER_KWH = 0.12;

    private UUID clientId;
    private double currentUsage;
    private double currentCost;
    private long timestamp;

    public MeterReading(String clientId, double currentUsage, long timestamp) {
        this.clientId = UUID.fromString(clientId);
        this.currentUsage = currentUsage;
        this.currentCost = currentUsage * COST_PER_KWH;
        this.timestamp = timestamp;
    }

    public UUID getClientId() {
        return clientId;
    }

    public double getCurrentUsage() {
        return currentUsage;
    }

    public double getCurrentCost() {
        return currentCost;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String toJSON() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.writeValueAsString(this);
    }
}