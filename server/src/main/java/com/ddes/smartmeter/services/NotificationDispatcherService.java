package com.ddes.smartmeter.services;

import com.ddes.smartmeter.entities.ListenerDetails;
import com.ddes.smartmeter.websockets.WebSocketResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.HashSet;
import java.util.Set;

@Service
public class NotificationDispatcherService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationDispatcherService.class);

    private final SimpMessagingTemplate template;

    private Set<ListenerDetails> listeners = new HashSet<>();

    public NotificationDispatcherService(SimpMessagingTemplate template) {
        this.template = template;
    }

    public void add(ListenerDetails details) {
        listeners.add(details);
    }

    public void remove(ListenerDetails details) {
        listeners.remove(details);
    }

    public Set<ListenerDetails> getListeners() {
        return listeners;
    }

    public void dispatchGridMessage() {

        LOGGER.info("Sending notification to all listeners");

        template.convertAndSend(
                "/notification/grid",
                new WebSocketResponse("Electricity Grid Down"));

    }

    public void dispatchMeterReading(String sessionId, String message) {

        LOGGER.info("Sending notification to clientId: ", sessionId);

        template.convertAndSendToUser(
                sessionId,
                "/notification/readingResult",
                new WebSocketResponse(message));

    }

    @EventListener
    public void sessionDisconnectionHandler(SessionDisconnectEvent event) {
        String sessionId = event.getSessionId();
        LOGGER.info("Disconnecting " + sessionId + "!");
        remove(listeners.stream().filter(listener -> listener.getSessionId().equals(sessionId)).findFirst().get());
    }
}
