package com.mv.MVCP.webSocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // Enable a simple memory-based message broker for both chat and notifications
        registry.enableSimpleBroker("/topic", "/topic2"); // Add both prefixes
        registry.setApplicationDestinationPrefixes("/app", "/app2"); // Add both prefixes
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Register WebSocket endpoints for both chat and notifications
        registry.addEndpoint("/chat", "/notifications") // Add both endpoints
                .setAllowedOrigins("http://localhost:8080")
                .withSockJS();
    }
}