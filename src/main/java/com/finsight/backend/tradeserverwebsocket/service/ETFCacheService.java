package com.finsight.backend.tradeserverwebsocket.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finsight.backend.tradeserverwebsocket.dto.ProductWebSocketDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ETFCacheService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    private final ETFCache etfCache;
    private final ObjectMapper objectMapper;
    private final ETFCalculationService etfCalculationService;

    public void updateFromPayload(String payload) {
        try {
            JsonNode root = objectMapper.readTree(payload);
            JsonNode dataArray = root.get("data");

            if (dataArray == null || !dataArray.isArray()) return;

            for (JsonNode data : dataArray) {
                String code = data.path("product_code").asText();
                double price = data.path("price").asDouble();
                double volume = data.path("volume").asDouble();
                long timestamp = data.has("timestamp") && !data.path("timestamp").isNull()
                        ? data.path("timestamp").asLong()
                        : System.currentTimeMillis();

                etfCache.update(code, price, volume, timestamp);

                sendUpdateToSubscribers(code);
            }
        } catch (Exception e) {
            log.error("[ETFCacheService] 캐시 저장 중 예외 발생", e);
        }
    }

    private void sendUpdateToSubscribers(String productCode) {
        ProductWebSocketDto dto = etfCalculationService.calculateDto(productCode);
        if (dto == null) return;

        String destination = "/topic/etf/" + productCode;
        messagingTemplate.convertAndSend(destination, dto);
        log.debug("[ETFCacheService] {}로 DTO 전송 완료: {}", destination, dto);
    }
}
