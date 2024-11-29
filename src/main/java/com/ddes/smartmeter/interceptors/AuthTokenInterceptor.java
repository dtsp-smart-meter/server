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

/**
 * Intercepts incoming WebSocket messages to validate the authentication token.
 * If an invalid or missing authentication token is detected, an error is logged,
 * and an event is published notifying the client that the authentication token is
 * invalid.
 */
@Component
public class AuthTokenInterceptor implements ChannelInterceptor {

    // Logger instance for logging messages and errors.
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthTokenInterceptor.class);

    // Event publisher used to publish events, such as invalid authentication token events.
    @Autowired
    private ApplicationEventPublisher eventPublisher;

    // Configuration class that contains valid authentication tokens.
    @Autowired
    private AuthTokenConfig authTokenConfig;

    /**
     * Intercepts incoming WebSocket messages before they are sent to the message handler.
     * This method checks if the message contains a valid authentication token.
     * If the token is invalid or missing, it logs the error and publishes an InvalidAuthTokenEvent.
     */
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        // Wrap the message to access its headers.
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);

        // Check if the STOMP command is SEND (rather than CONNECT or SUBSCRIBE).
        if (StompCommand.SEND.equals(headerAccessor.getCommand())) {
            // Retrieve clientId and authToken from the message headers.
            String clientId = headerAccessor.getFirstNativeHeader("clientId");
            String authToken = headerAccessor.getFirstNativeHeader("authToken");

            // If the token is invalid or missing, log the error and publish an event.
            if (!isValidToken(authToken)) {
                LOGGER.error("Invalid or missing authentication token for client " + clientId + ".");

                // Create an error message in JSON format.
                ObjectMapper mapper = new ObjectMapper();
                ObjectNode jsonObject = mapper.createObjectNode();
                jsonObject.put("message", "Invalid or missing authentication token.");

                // Publish an event indicating an invalid authentication token.
                eventPublisher.publishEvent(new InvalidAuthTokenEvent(this, clientId, jsonObject.toString()));

                // Return null to prevent further processing of the message.
                return null;
            }
        }

        // If the token is valid, proceed with the message.
        return message;
    }

    /**
     * Checks if the provided authentication token is valid.
     * The token is considered valid if it exists in the list of authorized tokens from the configuration.
     */
    private boolean isValidToken(String authToken) {
        // Check if the authentication token is present in the list of valid tokens.
        return authTokenConfig.getAuthTokens().contains(authToken);
    }
}