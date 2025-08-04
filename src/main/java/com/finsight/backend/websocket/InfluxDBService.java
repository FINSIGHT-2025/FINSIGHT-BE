package com.finsight.backend.websocket;

import com.influxdb.client.DeleteApi;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.domain.DeletePredicateRequest;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Service
public class InfluxDBService {

    private final InfluxDBClient influxDBClient;

    private final String ORG = "finsight";        // organization 명
    private final String BUCKET = "finsight-data"; // bucket 명

    private static final List<String> ETF_MEASUREMENTS = List.of("etf_price", "etf_volume");

    public InfluxDBService(InfluxDBClient influxDBClient) {
        this.influxDBClient = influxDBClient;
    }

    public void clearEtfMeasurements() {
        for (String measurement : ETF_MEASUREMENTS) {
            try {
                clearMeasurement(measurement);
            } catch (Exception e) {
                System.err.printf("[InfluxDBService] '%s' 삭제 중 오류 발생: %s%n", measurement, e.getMessage());
            }
        }
    }

    public void clearMeasurement(String measurement) throws Exception {
        DeleteApi deleteApi = influxDBClient.getDeleteApi();

        DeletePredicateRequest predicate = new DeletePredicateRequest();
        predicate.setStart(OffsetDateTime.ofInstant(Instant.EPOCH, ZoneOffset.UTC));
        predicate.setStop(OffsetDateTime.now(ZoneOffset.UTC));
        predicate.setPredicate(String.format("_measurement=\"%s\"", measurement));

        deleteApi.delete(predicate, BUCKET, ORG );  // 조직명, 버킷명 분명히 구분
        System.out.printf("[InfluxDBService] '%s' 측정값 삭제 완료%n", measurement);
    }
}

