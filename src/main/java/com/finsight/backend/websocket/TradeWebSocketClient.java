package com.finsight.backend.websocket;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.WebSocketSession;
import javax.annotation.PostConstruct;
import java.net.URI;

@Component
public class TradeWebSocketClient {

    private final TradeWebSocketHandler handler;
    private final InfluxDBService influxDBService;
    private final TradeService tradeService;

    public TradeWebSocketClient(TradeWebSocketHandler handler, InfluxDBService influxDBService, TradeService tradeService) {
        this.handler = handler;
        this.influxDBService = influxDBService;
        this.tradeService = tradeService;
    }

    @PostConstruct
    public void connect() {
        new Thread(() -> {
            try {
                influxDBService.clearEtfMeasurements(); // 1. DB 초기화
                Thread.sleep(3000);                     // 2. 대기

                // 3. 과거 ETF 데이터 비동기 저장
                tradeService.fetchAndSaveHistoricalData();

                // 4. WebSocket 연결 반복
                StandardWebSocketClient client = new StandardWebSocketClient();
                while (true) {
                    try {
                        WebSocketSession session = client
                                .doHandshake(handler, new WebSocketHttpHeaders(), URI.create("ws://localhost:3000"))
                                .get();

                        while (session.isOpen()) {
                            Thread.sleep(1000);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    System.out.println("[WebSocketClient] 5초 뒤 재시도합니다.");
                    Thread.sleep(60000); // 5. 60초 후 재시도
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}

