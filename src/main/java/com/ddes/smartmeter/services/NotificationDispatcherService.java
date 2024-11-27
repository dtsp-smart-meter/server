package com.ddes.smartmeter.services;

import com.ddes.smartmeter.events.InvalidAuthTokenEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

/**
 * Service class responsible for dispatching notifications to clients.
 */
@Service
public class NotificationDispatcherService {

    // Logger instance for logging messages and errors.
    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationDispatcherService.class);

    // Template for sending messages to clients.
    @Autowired
    private final SimpMessagingTemplate template;

    public NotificationDispatcherService(SimpMessagingTemplate template) {
        this.template = template;
    }

    /**
     * Event listener method that listens for InvalidAuthTokenEvent.
     * When an InvalidAuthTokenEvent is published, this method is invoked to dispatch a notification
     * to the client regarding the invalid authentication token.
     */
    @EventListener
    public void handleInvalidTokenEvent(InvalidAuthTokenEvent event) {
        // Dispatch notification to the specific client about the invalid token.
        dispatchNotification("alert/" + event.getClientId(), event.getMessage());
    }

    /**
     * Sends a notification message to a specific destination.
     */
    public void dispatchNotification(String destination, String message) {
        // Prefix the destination with "/notification/" to create the full destination path.
        destination = "/notification/" + destination;

        // Log the sending of the message to the specified destination.
        LOGGER.info("Sending message on channel: " + destination);

        // Send the message to the WebSocket destination.
        template.convertAndSend(destination, message);
    }
}