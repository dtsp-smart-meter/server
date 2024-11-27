package com.ddes.smartmeter.config;

import com.ddes.smartmeter.interceptors.AuthTokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * Configuration class for WebSocket and STOMP message broker setup.
 * This class configures WebSocket endpoints, message broker settings, and channel interceptors.
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    // Autowired interceptor for validating authentication tokens on inbound WebSocket messages.
    @Autowired
    private AuthTokenInterceptor authTokenInterceptor;

    /**
     * Configures the message broker for handling WebSocket messages.
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Enables a simple in-memory message broker for messages with the "/notification" prefix.
        config.enableSimpleBroker("/notification");
        // Sets the application destination prefix for routing messages to message-handling methods.
        config.setApplicationDestinationPrefixes("/app");
    }

    /**
     * Registers the STOMP WebSocket endpoint.
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Registers a WebSocket endpoint at "/ws" for clients to connect.
        registry.addEndpoint("/ws");
    }

    /**
     * Configures the inbound channel for WebSocket messages.
     * Adds the AuthTokenInterceptor to validate authentication tokens on incoming WebSocket messages.
     */
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        // Adds the authentication token interceptor for client messages.
        registration.interceptors(authTokenInterceptor);
    }
}