package com.honlife.core.infra.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.beans.factory.annotation.Value;


@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

  @Value("${front-server.prod-domain}")
  private String frontProdServer;

  @Value("${app.domain}")
  private String appDomain;



  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    registry.addEndpoint("/ws/connect")//웹소켓 연결할때 사용할 api
        .setAllowedOriginPatterns("*")
        .withSockJS();
  }



  @Override
  public void configureMessageBroker(MessageBrokerRegistry config) {
    config.enableSimpleBroker("/topic")
        .setHeartbeatValue(new long[]{10000, 10000});
    config.setApplicationDestinationPrefixes("/app");
  }


}