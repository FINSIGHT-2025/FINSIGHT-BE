package com.finsight.backend.tradeserverwebsocket.handler;

import com.finsight.backend.tradeserverwebsocket.service.ETFCacheService;
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
    private final ETFCacheService etfCacheService;

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        String payload = message.getPayload();
        etfCacheService.updateFromPayload(payload);
        tradeServerService.handleTextMessage(payload);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        tradeServerService.handleConnectionEstablished(session.getUri().toString());
        tradeServerService.fetchAndSaveHistoricalData();
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) {
        tradeServerService.handleConnectionClosed(status.toString());
    }
}

