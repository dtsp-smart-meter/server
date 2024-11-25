package com.ddes.smartmeter.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OutageAlertService {

    @Autowired
    private NotificationDispatcherService notificationDispatcher;

    public void dispatchAlert() throws JsonProcessingException {
        ObjectNode jsonObject = createAlert();

        notificationDispatcher.dispatchNotification("alert", jsonObject.toString());
    }

    public ObjectNode createAlert() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode jsonObject = mapper.createObjectNode();
        jsonObject.put("message", "Electricity grid is down.");
        return jsonObject;
    }
}