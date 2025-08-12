package com.finsight.backend.tradeserverwebsocket.client;

import com.finsight.backend.tradeserverwebsocket.handler.TradeWebSocketHandler;
import com.finsight.backend.tradeserverwebsocket.service.EtfPriceService;
import com.finsight.backend.tradeserverwebsocket.service.InfluxDBService;
import com.finsight.backend.tradeserverwebsocket.service.TradeServerService;
import com.finsight.backend.tradeserverwebsocket.service.scheduler.EtfCacheScheduler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;

import javax.annotation.PostConstruct;
import java.net.URI;

@Slf4j
@Component
@RequiredArgsConstructor
public class TradeWebSocketClient {

    private final TradeWebSocketHandler handler;
    private final InfluxDBService influxDBService;
    private final TradeServerService tradeServerService;
    private final WebSocketClient webSocketClient;
    private final EtfCacheScheduler etfCacheScheduler;

    @Value("${tradedata.url}")
    private String webSocketUrl;

    @PostConstruct
    public void connect() {
        String wsUrl = webSocketUrl.replaceFirst("^https?://", "wss://") + "/ws/local/ws";
        new Thread(() -> {
            boolean historicalDataFetched = false;
            try {
                log.info("[시세 서버] InfluxDB ETF 측정값 초기화 시작");
                influxDBService.deleteMeasurement("etf_price");
                influxDBService.deleteMeasurement("etf_volume");

                Thread.sleep(3000);

                while (true) {
                    try {
                        log.info("[시세 서버] WebSocket 연결 시도 중...");
                        WebSocketSession session = webSocketClient
                                .doHandshake(handler, new WebSocketHttpHeaders(), URI.create(wsUrl))
                                .get();

                        log.info("[시세 서버] WebSocket 연결 성공");

                        // 연결 성공 후 딱 한 번 실행
                        if (!historicalDataFetched) {
                            log.info("[시세 서버] 과거 ETF 데이터 비동기 저장 시작");
                            tradeServerService.fetchAndSaveHistoricalData();
                            historicalDataFetched = true;
                            Thread.sleep(30000);
                            etfCacheScheduler.updateAllEtfHistoricalPrices();
                        }

                        while (session.isOpen()) {
                            Thread.sleep(1000);
                        }

                        log.warn("[시세 서버] 세션이 닫혔습니다. 재연결 시도 예정");

                    } catch (Exception e) {
                        log.error("[시세 서버] WebSocket 연결 중 예외 발생: {} - {}", e.getClass().getSimpleName(), e.getMessage());
                    }

                    Thread.sleep(60000); // 재연결까지 대기 시간
                }
            } catch (InterruptedException e) {
                log.error("[시세 서버] WebSocket 클라이언트 스레드 중단", e);
                Thread.currentThread().interrupt();
            }
        }).start();
    }
}
