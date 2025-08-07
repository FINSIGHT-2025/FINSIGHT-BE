package com.finsight.backend.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

/**
 * 메시지 브로커 종류 : Rabbit MQ, Radis Pub/Sub등이 있다
 * 스프링에서 제공하는 내장 메시지 브로커 로는 Simple Broker가 제공된다.
 * Spring의 MessageBrokerRegistry를 사용하여 메시지 브설정한다
 */
@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    //채팅방 이름 설정
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {

        //1.메시지 브로커 경로를 설정한다
        /* 주로 사용하는 경로명은 다음과 같다
| 경로    | 용도
| ------ | ---------------------------------------------
| /topic | 브로드캐스트 방식 – 메시지를 여러 구독자에게 동시에 전송 (예: 채팅방) @SendTo
| /queue | 1:1 전송 (P2P)방식 – 특정 사용자에게만 전송 (예: 알림, 귓속말) @SendToUser

주의! 경로명은 이름일뿐.
      '/topic'이라서 브로드캐스트 되는 것이 아니라
      @SendTo("/topic/xxx")의 구독자가 여럿일 때 브로드캐스트 된다
          */
        config.enableSimpleBroker("/topic", "/queue");

        //2.메시지 브로커의 STOMP용 endpoint를 설정한다
        config.setApplicationDestinationPrefixes("/app");
    }


    /**
     * 클라이언트가 WebSocket 연결할 수 있는 엔드포인트를 설정
     * @param registry
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-etf")
                .setAllowedOrigins("http://localhost:5173", "http://localhost:8080")
                .withSockJS();
    }
}
