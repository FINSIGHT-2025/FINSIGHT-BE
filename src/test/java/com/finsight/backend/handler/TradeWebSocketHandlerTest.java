package com.finsight.backend.handler;

import com.finsight.backend.websocket.TradeService;
import com.finsight.backend.websocket.TradeWebSocketHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.CloseStatus;

import static org.mockito.Mockito.*;

public class TradeWebSocketHandlerTest {

    private TradeService tradeService;
    private TradeWebSocketHandler handler;

    @BeforeEach
    public void setUp() {
        tradeService = mock(TradeService.class);
        handler = new TradeWebSocketHandler(tradeService);
    }

    @Test
    public void testHandleTextMessage_callsService() throws Exception {
        // given
        WebSocketSession session = mock(WebSocketSession.class);
        TextMessage message = new TextMessage("sample message");

        // when
        handler.handleTextMessage(session, message);

        // then
        verify(tradeService, times(1)).handleTextMessage("sample message");
    }

    @Test
    public void testAfterConnectionEstablished_callsService() throws Exception {
        // given
        WebSocketSession session = mock(WebSocketSession.class);
        when(session.getUri()).thenReturn(new java.net.URI("ws://localhost:3000/ws/trade"));

        // when
        handler.afterConnectionEstablished(session);

        // then
        verify(tradeService, times(1)).handleConnectionEstablished("ws://localhost:3000/ws/trade");
    }

    @Test
    public void testAfterConnectionClosed_callsService() throws Exception {
        // given
        WebSocketSession session = mock(WebSocketSession.class);
        CloseStatus status = new CloseStatus(1000, "Normal Closure");

        // when
        handler.afterConnectionClosed(session, status);

        // then
        verify(tradeService, times(1)).handleConnectionClosed(status.toString());
    }
}
