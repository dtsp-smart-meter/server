package com.ddes.smartmeter.services;

import com.ddes.smartmeter.events.InvalidAuthTokenEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
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

    @EventListener
    public void handleInvalidTokenEvent(InvalidAuthTokenEvent event) {
        dispatchNotification("alert/" + event.getClientId(), event.getMessage());
    }

    public void dispatchNotification(String destination, String message) {
        destination = "/notification/" + destination;

        LOGGER.info("Sending message on channel: " + destination);

        template.convertAndSend(destination, message);
    }
}