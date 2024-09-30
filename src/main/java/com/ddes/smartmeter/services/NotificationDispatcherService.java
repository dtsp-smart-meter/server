package com.ddes.smartmeter.services;

import com.ddes.smartmeter.websockets.WebSocketResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationDispatcherService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationDispatcherService.class);

    @Autowired
    private final SimpMessagingTemplate template;

    public NotificationDispatcherService(SimpMessagingTemplate template) {
        this.template = template;
    }

    public void dispatchMeterReading(String clientId, String message) {
        String destination = "/readingResult/" + clientId;

        LOGGER.info("Sending message to client: " + clientId + " on channel: " + destination);

        template.convertAndSend(destination, new WebSocketResponse("Message from RabbitMQ: " + message));
    }
}
