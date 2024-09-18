package com.ddes.smartmeter.websockets;

import com.ddes.smartmeter.entities.MeterReading;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class MeterReadingController {

    @MessageMapping("/meterReading")
    @SendTo("/topic/smartMeter")
    public MeterReadingResponse meterReadingResponse(MeterReading meterReading) throws Exception {
        return new MeterReadingResponse("Meter reading recieved.");
    }
}