package com.finsight.backend.tradeserverwebsocket.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finsight.backend.service.InfluxWriteService;
import com.finsight.backend.tradeserverwebsocket.dto.EtfPriceDTO;
import com.finsight.backend.tradeserverwebsocket.dto.EtfVolumeDTO;
import com.finsight.backend.tradeserverwebsocket.wrapper.ETFPriceResponseWrapper;
import com.finsight.backend.tradeserverwebsocket.wrapper.ETFVolumeResponseWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TradeServerService {

    private static final DateTimeFormatter TIMESTAMP_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;
    private final InfluxWriteService influxWriteService;

    @Value("${tradedata.url}")
    private String tradeDataUrl;

    public void handleConnectionEstablished(String sessionInfo) {
        log.info("[WebSocket] 연결됨: {}", sessionInfo);
    }

    public void handleConnectionClosed(String status) {
        log.info("[WebSocket] 연결 종료: {}", status);
    }

    public void handleTradeMessage(String payload) {
        log.info("[TradeService] 수신된 메시지: {}", payload);
    }

    public void handleTextMessage(String payload) {
        try {
            JsonNode root = mapper.readTree(payload);
            JsonNode dataArray = root.get("data");

            if (dataArray == null || !dataArray.isArray()) {
                log.warn("[TradeService] InfluxDB 저장 실패: data 배열 없음");
                return;
            }

            for (JsonNode data : dataArray) {
                String productCode = data.path("product_code").asText(null);
                double price = data.path("price").asDouble(Double.NaN);
                long volume = data.path("volume").asLong(0);
                String timestampStr = data.path("timestamp").asText(null);

                if (productCode == null || timestampStr == null || Double.isNaN(price) || volume < 0) {
                    log.warn("[TradeService] InfluxDB 저장 실패: 필수 데이터 누락 (productCode: {}, timestamp: {}, price: {}, volume: {})",
                            productCode, timestampStr, price, volume);
                    continue;
                }

                Instant timestamp = Instant.parse(timestampStr);

                influxWriteService.writeEtfPrice(productCode, price, timestamp);
                influxWriteService.writeEtfVolume(productCode, volume, timestamp);
            }

        } catch (Exception e) {
            log.error("[TradeService] InfluxDB 저장 중 예외 발생", e);
        }
    }

    @Async
    public void fetchAndSaveHistoricalData() {
        try {
            ZonedDateTime nowKST = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
            String timestampParam = nowKST.format(TIMESTAMP_FORMATTER);

            String priceUrl = tradeDataUrl + "/etf/etf_price/prev?timestamp=" + timestampParam;
            String volumeUrl = tradeDataUrl + "/etf/etf_volume/prev?timestamp=" + timestampParam;

            // 가격 저장
            String priceJson = restTemplate.getForObject(priceUrl, String.class);
            ETFPriceResponseWrapper priceWrapper = mapper.readValue(priceJson, ETFPriceResponseWrapper.class);
            List<EtfPriceDTO> priceData = priceWrapper.getData();

            for (EtfPriceDTO price : priceData) {
                Instant ts = Instant.parse(price.getTimestamp());
                influxWriteService.writeEtfPrice(price.getProductCode(), price.getEtfPrice(), ts);
            }

            // 거래량 저장
            String volumeJson = restTemplate.getForObject(volumeUrl, String.class);
            ETFVolumeResponseWrapper volumeWrapper = mapper.readValue(volumeJson, ETFVolumeResponseWrapper.class);
            List<EtfVolumeDTO> volumeData = volumeWrapper.getData();

            for (EtfVolumeDTO volume : volumeData) {
                Instant ts = Instant.parse(volume.getTimestamp());
                influxWriteService.writeEtfVolume(volume.getProductCode(), volume.getEtfVolume(), ts);
            }

            log.info("[TradeService] 과거 ETF 데이터 저장 완료");

        } catch (Exception e) {
            log.error("[TradeService] 과거 데이터 저장 중 오류 발생", e);
        }
    }
}
