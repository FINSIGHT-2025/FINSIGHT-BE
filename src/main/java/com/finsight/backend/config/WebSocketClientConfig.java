package com.finsight.backend.config;

import com.finsight.backend.websocket.InfluxDBService;
import com.finsight.backend.websocket.TradeService;
import com.finsight.backend.websocket.TradeWebSocketClient;
import com.finsight.backend.websocket.TradeWebSocketHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebSocketClientConfig {

    @Bean
    public TradeWebSocketHandler tradeWebSocketHandler(TradeService tradeService) {
        return new TradeWebSocketHandler(tradeService);
    }

    @Bean
    public TradeWebSocketClient tradeWebSocketClient(TradeWebSocketHandler handler, InfluxDBService influxDBService, TradeService tradeService) {
        return new TradeWebSocketClient(handler, influxDBService, tradeService);
    }
}
