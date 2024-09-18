package com.ddes.smartmeter.websockets;

public class MeterReadingResponse {

    private String message;

    public MeterReadingResponse() {}

    public MeterReadingResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
