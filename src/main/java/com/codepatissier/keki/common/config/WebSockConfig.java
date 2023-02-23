package com.codepatissier.keki.common.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.*;

@RequiredArgsConstructor
@Configuration
@EnableWebSocketMessageBroker
public class WebSockConfig implements WebSocketMessageBrokerConfigurer {

    // 웹 소켓 주소 변경
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("ws/chat").setAllowedOrigins("*");
    }

    //    @Override
//    public void register(WebSocketHandlerRegistry registry) {
//        registry.addHandler(webSocketHandler, "ws/chat").setAllowedOrigins("*");
//    }
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/pub"); //클라이언트에서 메세지 보낼 주소
        registry.enableSimpleBroker("/sub"); // 구독하고 있는 클라이언트에게 메세지 전달할 주소
    }
}