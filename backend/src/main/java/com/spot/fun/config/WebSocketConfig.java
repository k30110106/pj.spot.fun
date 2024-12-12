package com.spot.fun.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@RequiredArgsConstructor
@EnableWebSocketMessageBroker // 웹소켓 메시지 핸들링 활성화
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    // stomp 접속 주소 url = ws://localhost:8080/ws, 프로토콜이 http가 아니다!
    registry.addEndpoint("/ws").withSockJS(); //버전 낮은 브라우저에서도 적용 가능(SockJS가 그렇게 만들어준다는 듯?)
  }

  @Override
  public void configureMessageBroker(MessageBrokerRegistry registry) {
    registry.enableSimpleBroker("/sub/user", "/sub/other");   // 메시지를 구독(수신)하는 요청 엔드포인트
    registry.setApplicationDestinationPrefixes("/pub");   // 메시지를 발행(송신)하는 엔드포인트
  }
}
