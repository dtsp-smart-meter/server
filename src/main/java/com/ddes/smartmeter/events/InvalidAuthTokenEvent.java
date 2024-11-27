package com.ddes.smartmeter.events;

import org.springframework.context.ApplicationEvent;

/**
 * Event to represent an invalid authentication token.
 * This event is published when an invalid authentication token is detected for a given client.
 * It carries information about the client and the error message.
 */
public class InvalidAuthTokenEvent extends ApplicationEvent {

    // The client ID associated with the invalid authentication token.
    private final String clientId;

    // The error message associated with the invalid token event.
    private final String message;

    /**
     * Constructor to initialize the event with source, client ID, and message.
     */
    public InvalidAuthTokenEvent(Object source, String clientId, String message) {
        super(source);
        this.clientId = clientId;
        this.message = message;
    }

    /**
     * Getter for the client ID.
     */
    public String getClientId() {
        return clientId;
    }

    /**
     * Getter for the error message.
     */
    public String getMessage() {
        return message;
    }
}