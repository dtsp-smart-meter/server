package com.ddes.smartmeter.services;

import com.ddes.smartmeter.websockets.WebSocketResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class OutageMethodCallerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OutageMethodCallerService.class);
    private final Random random = new Random();

    private final SimpMessagingTemplate template;

    public OutageMethodCallerService(SimpMessagingTemplate template) {
        this.template = template;
    }

    @Scheduled(fixedRate = 10000) // Runs every 10 seconds
    public void scheduleTaskWithRandomDelay() {
        int delay = random.nextInt(10000); // Random delay up to 10 seconds
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            LOGGER.error("Task interrupted", e);
        }
        dispatchOutageAlert(message);
    }

    public void dispatchOutageAlert(String message) {
        String destination = "/outageAlert";

        LOGGER.info("Sending message to all clients on channel: " + destination);

        template.convertAndSend(destination, new WebSocketResponse(message));
    }
}
