package com.ddes.smartmeter.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.UUID;

public class MeterReading {

    private static final double COST_PER_KWH = 0.12;

    private UUID clientId;
    private double currentUsage;
    private double currentCost;
    private long timestamp;

    public MeterReading() {}

    @JsonCreator // Allows Jackson to use this constructor for deserialization
    public MeterReading(@JsonProperty("clientId") String clientId, @JsonProperty("currentUsage") double currentUsage, @JsonProperty("timestamp") long timestamp) {
        this.clientId = clientId != null ? UUID.fromString(clientId) : null;
        this.currentUsage = currentUsage;
        this.currentCost = currentUsage * COST_PER_KWH;
        this.timestamp = timestamp;
    }

    public UUID getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = UUID.fromString(clientId);
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