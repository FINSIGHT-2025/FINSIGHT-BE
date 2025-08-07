package com.finsight.backend.tradeserverwebsocket.handler;

import com.finsight.backend.tradeserverwebsocket.service.EtfCacheService;
import com.finsight.backend.tradeserverwebsocket.service.TradeServerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
@RequiredArgsConstructor
public class TradeWebSocketHandler extends TextWebSocketHandler {

    private final TradeServerService tradeServerService;
    private final EtfCacheService etfCacheService;

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        String payload = message.getPayload();
        etfCacheService.updateFromPayload(payload);
        tradeServerService.handleTextMessage(payload);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String uri = session.getUri() != null ? session.getUri().toString() : "unknown";
        tradeServerService.handleConnectionEstablished(uri);
        tradeServerService.fetchAndSaveHistoricalData();
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) {
        tradeServerService.handleConnectionClosed(status.toString());
    }
}

