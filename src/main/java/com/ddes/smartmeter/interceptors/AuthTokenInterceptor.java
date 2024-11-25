package com.ddes.smartmeter.interceptors;

import com.ddes.smartmeter.config.AuthTokenConfig;
import com.ddes.smartmeter.events.InvalidAuthTokenEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Component
public class AuthTokenInterceptor implements ChannelInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthTokenInterceptor.class);

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private AuthTokenConfig authTokenConfig;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);

        // Check if the Stomp command is SEND
        if (StompCommand.SEND.equals(headerAccessor.getCommand())) {
            String clientId = headerAccessor.getFirstNativeHeader("clientId");
            String authToken = headerAccessor.getFirstNativeHeader("authToken");

            if (!isValidToken(authToken)) {
                LOGGER.error("Invalid or missing authentication token for client " + clientId + ".");

                ObjectMapper mapper = new ObjectMapper();
                ObjectNode jsonObject = mapper.createObjectNode();
                jsonObject.put("message", "Invalid or missing authentication token.");

                eventPublisher.publishEvent(new InvalidAuthTokenEvent(this, clientId, jsonObject.toString()));

                return null;
            }
        }

        return message;
    }

    private boolean isValidToken(String authToken) {
        return authTokenConfig.getAuthTokens().contains(authToken);
    }
}