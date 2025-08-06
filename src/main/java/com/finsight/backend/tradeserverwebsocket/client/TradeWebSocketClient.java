package com.finsight.backend.tradeserverwebsocket.client;

import com.finsight.backend.tradeserverwebsocket.handler.TradeWebSocketHandler;
import com.finsight.backend.tradeserverwebsocket.service.InfluxDBService;
import com.finsight.backend.tradeserverwebsocket.service.TradeServerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.PostConstruct;
import java.net.URI;

@Slf4j
@Component
public class TradeWebSocketClient {

    private final TradeWebSocketHandler handler;
    private final InfluxDBService influxDBService;
    private final TradeServerService tradeServerService;

    public TradeWebSocketClient(TradeWebSocketHandler handler, InfluxDBService influxDBService, TradeServerService tradeServerService) {
        this.handler = handler;
        this.influxDBService = influxDBService;
        this.tradeServerService = tradeServerService;
    }

    @PostConstruct
    public void connect() {
        new Thread(() -> {
            try {
                log.info("[TradeWebSocketClient] InfluxDB ETF 측정값 초기화 시작");
                influxDBService.clearEtfMeasurements();

                Thread.sleep(3000);

                log.info("[TradeWebSocketClient] 과거 ETF 데이터 비동기 저장 시작");
                tradeServerService.fetchAndSaveHistoricalData();

                StandardWebSocketClient client = new StandardWebSocketClient();
                while (true) {
                    try {
                        log.info("[TradeWebSocketClient] WebSocket 연결 시도 중...");
                        WebSocketSession session = client
                                .doHandshake(handler, new WebSocketHttpHeaders(), URI.create("ws://localhost:3000/ws/local/ws"))
                                .get();

                        log.info("[TradeWebSocketClient] WebSocket 연결 성공");

                        while (session.isOpen()) {
                            Thread.sleep(1000);
                        }

                        log.warn("[TradeWebSocketClient] 세션이 닫혔습니다. 재연결 시도 예정");

                    } catch (Exception e) {
                        log.error("[TradeWebSocketClient] WebSocket 연결 중 예외 발생", e);
                    }

                    log.info("[TradeWebSocketClient] 60초 후 WebSocket 재연결 시도");
                    Thread.sleep(60000);
                }

            } catch (InterruptedException e) {
                log.error("[TradeWebSocketClient] WebSocket 클라이언트 스레드 중단", e);
                Thread.currentThread().interrupt();
            }
        }).start();
    }
}
