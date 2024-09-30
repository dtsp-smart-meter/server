package com.ddes.smartmeter.websockets;

public class WebSocketResponse {

    private String message;

    public WebSocketResponse() {}

    public WebSocketResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
