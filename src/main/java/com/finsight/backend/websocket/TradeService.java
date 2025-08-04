package com.finsight.backend.websocket;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.write.Point;
import com.influxdb.client.domain.WritePrecision;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class TradeService {

    private static final Logger logger = LoggerFactory.getLogger(TradeService.class);
    private final RestTemplate restTemplate = new RestTemplate();
    private static final List<String> MEASUREMENTS = List.of("etf_price", "etf_volume");
    private static final String API_BASE_URL = "http://localhost:3000/etf";

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final InfluxDBClient influxDBClient;
    private final WriteApiBlocking writeApi;
    private final ObjectMapper mapper = new ObjectMapper();

    private final String bucket;
    private final String org;

    public TradeService(
            InfluxDBClient influxDBClient,
            @Value("${influx.bucket:finsight-data}") String bucket,
            @Value("${influx.org:finsight}") String org
    ) {
        this.influxDBClient = influxDBClient;
        this.writeApi = influxDBClient.getWriteApiBlocking();
        this.bucket = bucket;
        this.org = org;
    }

    public void handleConnectionEstablished(String sessionInfo) {
        logger.info("[WebSocket] 연결됨: {}", sessionInfo);
    }

    public void handleConnectionClosed(String status) {
        logger.info("[WebSocket] 연결 종료: {}", status);
    }

    public void handleTradeMessage(String payload) {
        logger.info("[TradeService] 수신된 메시지: {}", payload);
    }

    public void handleTextMessage(String payload) {
        try {
            JsonNode root = mapper.readTree(payload);
            JsonNode dataArray = root.get("data");

            if (dataArray == null || !dataArray.isArray()) {
                System.out.println("[TradeService] InfluxDB 저장 실패: data 배열 없음");
                return;
            }

            for (JsonNode data : dataArray) {
                String productCode = data.path("product_code").asText(null);
                double price = data.path("price").asDouble(Double.NaN);
                double volume = data.path("volume").asDouble(Double.NaN);
                String timestampStr = data.path("timestamp").asText(null);

                if (productCode == null || timestampStr == null || Double.isNaN(price) || Double.isNaN(volume)) {
                    System.out.printf("[TradeService] InfluxDB 저장 실패: 필수 데이터 누락 (productCode: %s, timestamp: %s, price: %s, volume: %s)%n",
                            productCode, timestampStr, price, volume);
                    continue;
                }

                Instant timestamp = Instant.parse(timestampStr);

                Point pricePoint = Point.measurement("etf_price")
                        .addTag("product_code", productCode)
                        .addField("value", price)
                        .time(timestamp, WritePrecision.MS);

                Point volumePoint = Point.measurement("etf_volume")
                        .addTag("product_code", productCode)
                        .addField("value", volume)
                        .time(timestamp, WritePrecision.MS);

                writeApi.writePoints(bucket, org, List.of(pricePoint, volumePoint));
            }

        } catch (Exception e) {
            System.out.println("[TradeService] InfluxDB 저장 중 예외 발생");
            e.printStackTrace();
        }
    }

    @Async
    public void fetchAndSaveHistoricalData() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);

            // 한국시간 기준 timestamp 생성
            ZonedDateTime nowKST = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
            String timestampParam = nowKST.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));

            // 예시 URL (timestamp 쿼리 파라미터 포함)
            String baseUrl = "http://localhost:3000/etf/etf_price/prev?timestamp=" + timestampParam;
            String volumeUrl = baseUrl.replace("etf_price", "etf_volume");

            // price 데이터 가져오기
            ResponseEntity<String> priceResponse = restTemplate.getForEntity(baseUrl, String.class);
            ETFPriceResponseWrapper priceWrapper = mapper.readValue(priceResponse.getBody(), ETFPriceResponseWrapper.class);
            List<ETFPriceDTO> priceData = priceWrapper.getData();

            // volume 데이터 가져오기
            ResponseEntity<String> volumeResponse = restTemplate.getForEntity(volumeUrl, String.class);
            ETFVolumeResponseWrapper volumeWrapper = mapper.readValue(volumeResponse.getBody(), ETFVolumeResponseWrapper.class);
            List<ETFVolumeDTO> volumeData = volumeWrapper.getData();

            // InfluxDB 저장
            for (ETFPriceDTO price : priceData) {
                Instant ts = Instant.parse(price.getTimestamp());

                Point pricePoint = Point.measurement("etf_price")
                        .addTag("product_code", price.getProduct_code())
                        .addField("value", price.getEtf_price())
                        .time(ts, WritePrecision.MS);

                writeApi.writePoint(bucket, org, pricePoint);
            }

            for (ETFVolumeDTO volume : volumeData) {
                Instant ts = Instant.parse(volume.getTimestamp());

                Point volumePoint = Point.measurement("etf_volume")
                        .addTag("product_code", volume.getProduct_code())
                        .addField("value", volume.getEtf_volume())
                        .time(ts, WritePrecision.MS);

                writeApi.writePoint(bucket, org, volumePoint);
            }

            logger.info("[TradeService] 과거 ETF 데이터 저장 완료");

        } catch (Exception e) {
            logger.error("과거 데이터 저장 중 오류 발생", e);
        }
    }
}
