package com.finsight.backend.tradeserverwebsocket.service;

import com.finsight.backend.config.InfluxDBConfig;
import com.influxdb.client.DeleteApi;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.domain.DeletePredicateRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Service
public class InfluxDBService {

    private static final Logger log = LoggerFactory.getLogger(InfluxDBService.class);

    private final InfluxDBClient influxDBClient;
    private final String bucket;
    private final String org;

    private static final List<String> ETF_MEASUREMENTS = List.of("etf_price", "etf_volume");

    public InfluxDBService(InfluxDBClient influxDBClient,
                           InfluxDBConfig influxDBConfig) {
        this.influxDBClient = influxDBClient;
        this.bucket = influxDBConfig.getInfluxBucket();
        this.org = influxDBConfig.getInfluxOrg();
    }

    public void clearEtfMeasurements() {
        for (String measurement : ETF_MEASUREMENTS) {
            try {
                clearMeasurement(measurement);
            } catch (Exception e) {
                log.error("[InfluxDBService] '{}' 삭제 중 오류 발생: {}", measurement, e.getMessage(), e);
            }
        }
    }

    public void clearMeasurement(String measurement) throws Exception {
        DeleteApi deleteApi = influxDBClient.getDeleteApi();

        DeletePredicateRequest predicate = new DeletePredicateRequest();
        predicate.setStart(OffsetDateTime.ofInstant(Instant.EPOCH, ZoneOffset.UTC));
        predicate.setStop(OffsetDateTime.now(ZoneOffset.UTC));
        predicate.setPredicate(String.format("_measurement=\"%s\"", measurement));

        deleteApi.delete(predicate, bucket, org);
        log.info("[InfluxDBService] '{}' 측정값 삭제 완료", measurement);
    }
}
