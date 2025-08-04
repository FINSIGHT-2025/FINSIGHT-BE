package com.finsight.backend.websocket;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.time.Instant;

public class TradeWebSocketHandler extends TextWebSocketHandler {

    private final TradeService tradeService;

    public TradeWebSocketHandler(TradeService tradeService) {
        this.tradeService = tradeService;
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        String payload = message.getPayload();
        //tradeService.handleTradeMessage(payload);  // 콘솔 로깅
        tradeService.handleTextMessage(payload);   // InfluxDB 저장
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        tradeService.handleConnectionEstablished(session.getUri().toString());
        tradeService.fetchAndSaveHistoricalData(); // 과거 데이터 저장 로직 필요 시
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) {
        tradeService.handleConnectionClosed(status.toString());
    }
}
