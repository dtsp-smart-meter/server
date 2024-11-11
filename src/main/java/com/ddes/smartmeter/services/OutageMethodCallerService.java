package com.ddes.smartmeter.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class OutageMethodCallerService {

    @Autowired
    private NotificationDispatcherService notificationDispatcher;

    @Scheduled(fixedRate = 10000)
    public void scheduleTask() throws JsonProcessingException {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            exception.printStackTrace();
        }

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode jsonObject = mapper.createObjectNode();
        jsonObject.put("message", "Electricity grid is down.");

        notificationDispatcher.dispatchNotification("alert", mapper.writeValueAsString(jsonObject));
    }
}
