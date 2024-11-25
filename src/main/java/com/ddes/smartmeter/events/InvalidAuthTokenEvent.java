package com.ddes.smartmeter.events;

import org.springframework.context.ApplicationEvent;

public class InvalidAuthTokenEvent extends ApplicationEvent {

    private final String clientId;
    private final String message;

    public InvalidAuthTokenEvent(Object source, String clientId, String message) {
        super(source);

        this.clientId = clientId;
        this.message = message;
    }

    public String getClientId() {
        return clientId;
    }

    public String getMessage() {
        return message;
    }
}
