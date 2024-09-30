package com.ddes.smartmeter.entities;

public class ListenerDetails {

    private String clientId;

    private String sessionId;

    public ListenerDetails(String clientId, String sessionId) {
        this.clientId = clientId;
        this.sessionId = sessionId;
    }

    private ListenerDetails(Builder builder) {
        this.clientId = builder.clientId;
        this.sessionId = builder.sessionId;
    }

    public String getClientId() {
        return clientId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public static class Builder {
        private String clientId;
        private String sessionId;

        // Setter method for clientId
        public Builder setClientId(String clientId) {
            this.clientId = clientId;
            return this; // Return the builder instance for method chaining
        }

        // Setter method for sessionId
        public Builder setSessionId(String sessionId) {
            this.sessionId = sessionId;
            return this; // Return the builder instance for method chaining
        }

        // Build method to create a ListenerDetails instance
        public ListenerDetails build() {
            return new ListenerDetails(this);
        }
    }

}
