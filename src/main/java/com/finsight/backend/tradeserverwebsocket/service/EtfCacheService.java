package com.finsight.backend.tradeserverwebsocket.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finsight.backend.tradeserverwebsocket.dto.ProductWebSocketDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EtfCacheService {
    private static final String TIMESTAMP_FIELD = "timestamp";
    
    private final SimpMessagingTemplate messagingTemplate;
    private final EtfCache etfCache;
    private final ObjectMapper objectMapper;
    private final EtfCalculationService etfCalculationService;

    public void updateFromPayload(String payload) {
        try {
            JsonNode root = objectMapper.readTree(payload);
            JsonNode dataArray = root.get("data");

            if (dataArray == null || !dataArray.isArray()) return;

            for (JsonNode data : dataArray) {
                String code = data.path("product_code").asText();
                double price = data.path("price").asDouble();
                long volume = data.path("volume").asLong();
                long timestamp = data.has(TIMESTAMP_FIELD) && !data.path(TIMESTAMP_FIELD).isNull()
                        ? data.path(TIMESTAMP_FIELD).asLong()
                        : System.currentTimeMillis();

                etfCache.update(code, price, volume, timestamp);

                sendUpdateToSubscribers(code);
            }
        } catch (Exception e) {
            log.error("[EtfCacheService] 캐시 저장 중 예외 발생", e);
        }
    }

    private void sendUpdateToSubscribers(String productCode) {
        ProductWebSocketDTO dto = etfCalculationService.calculateDto(productCode);
        if (dto == null) return;

        String destination = "/topic/etf/" + productCode;
        messagingTemplate.convertAndSend(destination, dto);
//        log.debug("[EtfCacheService] {}로 DTO 전송 완료: {}", destination, dto);
    }
}
