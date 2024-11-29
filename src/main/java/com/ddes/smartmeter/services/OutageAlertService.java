package com.ddes.smartmeter.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service class responsible for dispatching outage alerts.
 * This service creates an alert message and uses the NotificationDispatcherService
 * to send the alert notification to all clients.
 */
@Service
public class OutageAlertService {

    // Service used to dispatch notifications to all clients.
    @Autowired
    private NotificationDispatcherService notificationDispatcher;

    /**
     * Dispatches an outage alert notification to all clients.
     * This method creates an alert message and sends it using the NotificationDispatcherService.
     */
    public void dispatchAlert() throws JsonProcessingException {
        // Create the alert message in JSON format.
        ObjectNode jsonObject = createAlert();

        // Dispatch the alert notification to clients via the notification dispatcher.
        notificationDispatcher.dispatchNotification("alert", jsonObject.toString());
    }

    /**
     * Creates the alert message in JSON format.
     */
    public ObjectNode createAlert() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode jsonObject = mapper.createObjectNode();

        // Add the alert message to the JSON object.
        jsonObject.put("message", "Electricity grid is down.");

        // Return the created JSON object.
        return jsonObject;
    }
}