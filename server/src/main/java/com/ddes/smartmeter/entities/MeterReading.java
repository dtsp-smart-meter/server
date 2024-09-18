package com.ddes.smartmeter.entities;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.TimeZone;
import java.util.UUID;

public class MeterReading {

    private UUID clientId;
    private double currentUsage;
    private LocalDateTime timestamp;

    public MeterReading(String clientId, double currentUsage, long timestamp) {
        this.clientId = UUID.fromString(clientId);
        this.currentUsage = currentUsage;
        this.timestamp = LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), TimeZone.getDefault().toZoneId());
    }

    public UUID getClientId() {
        return clientId;
    }

    public void setClientId(UUID clientId) {
        this.clientId = clientId;
    }

    public double getCurrentUsage() {
        return currentUsage;
    }

    public void setCurrentUsage(double currentUsage) {
        this.currentUsage = currentUsage;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}