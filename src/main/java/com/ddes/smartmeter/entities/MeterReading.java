package com.ddes.smartmeter.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.UUID;

/**
 * Represents a meter reading entity, containing information about energy usage, cost,
 * and the timestamp of the reading.
 */
public class MeterReading {

    // Constant representing the cost per kilowatt-hour (kWh) in GBP.
    private static final double COST_PER_KWH = 0.12;

    // UUID representing the unique ID of the client associated with this meter reading.
    private UUID clientId;

    // The energy usage recorded in kilowatt-hours.
    private double currentUsage;

    // The cost calculated based on the energy usage.
    private double currentCost;

    // The timestamp of the meter reading.
    private long timestamp;

    // Default constructor for creating an empty MeterReading instance.
    public MeterReading() {}

    /**
     * Constructor used for JSON deserialization.
     * Initializes a MeterReading instance with client ID, usage, and timestamp.
     * Automatically calculates the cost based on usage.
     */
    @JsonCreator // Allows Jackson to use this constructor for deserialization.
    public MeterReading(
            @JsonProperty("clientId") String clientId,
            @JsonProperty("currentUsage") double currentUsage,
            @JsonProperty("timestamp") long timestamp) {
        this.clientId = clientId != null ? UUID.fromString(clientId) : null; // Converts string to UUID if not null.
        this.currentUsage = currentUsage;
        this.currentCost = currentUsage * COST_PER_KWH; // Calculates cost based on usage and rate.
        this.timestamp = timestamp;
    }

    // Getter for the client ID.
    public UUID getClientId() {
        return clientId;
    }

    // Setter for the client ID, accepting a string and converting it to a UUID.
    public void setClientId(String clientId) {
        this.clientId = UUID.fromString(clientId);
    }

    // Getter for the current energy usage.
    public double getCurrentUsage() {
        return currentUsage;
    }

    // Getter for the calculated cost based on energy usage.
    public double getCurrentCost() {
        return currentCost;
    }

    // Getter for the timestamp of the meter reading.
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * Serializes the MeterReading instance into a JSON string.
     */
    public String toJSON() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper(); // Jackson ObjectMapper for JSON operations.
        return objectMapper.writeValueAsString(this);
    }
}