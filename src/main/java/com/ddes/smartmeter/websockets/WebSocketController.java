package com.ddes.smartmeter.websockets;

import com.ddes.smartmeter.entities.ListenerDetails;
import com.ddes.smartmeter.entities.MeterReading;
import com.ddes.smartmeter.services.NotificationDispatcherService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    private final RabbitTemplate rabbitTemplate;

    private final NotificationDispatcherService notificationDispatcher;

    @Autowired
    public WebSocketController(RabbitTemplate rabbitTemplate,
                               NotificationDispatcherService notificationDispatcher) {
        this.rabbitTemplate = rabbitTemplate;
        this.notificationDispatcher = notificationDispatcher;
    }

    @MessageMapping("/meterReading")
    @SendTo("/topic/smartMeter")
    public WebSocketResponse meterReadingResponse(MeterReading meterReading,
                                                  SimpMessageHeaderAccessor simpMessageHeaderAccessor) throws Exception {
        System.out.println("Client ID: " + meterReading.getClientId());
        System.out.println("Current Usage: " + meterReading.getCurrentUsage());
        System.out.println("Timestamp: " + meterReading.getTimestamp());

        ListenerDetails.Builder listenerDetailsBuilder = new ListenerDetails.Builder();

        ListenerDetails listenerDetails = listenerDetailsBuilder
                .setClientId(meterReading.getClientId().toString())
                .setSessionId(simpMessageHeaderAccessor.getSessionId())
                .build();

        System.out.println("Adding listener: " + listenerDetails.getClientId());

        notificationDispatcher.add(listenerDetails);

        rabbitTemplate.convertAndSend("meterReadings", meterReading.toJSON());

        return new WebSocketResponse("Meter reading recieved.");
    }
}