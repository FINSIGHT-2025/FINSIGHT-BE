package com.finsight.backend.tmptradeserverwebsocket.service;

import com.finsight.backend.config.InfluxDBConfig;
import com.influxdb.client.DeleteApi;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.domain.DeletePredicateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Slf4j
@Service
@RequiredArgsConstructor
public class InfluxDBService {

    private final InfluxDBClient influxDBClient;
    private final InfluxDBConfig influxDBConfig;

    public void deleteMeasurement(String measurement) {
        try {
            DeleteApi deleteApi = influxDBClient.getDeleteApi();

            OffsetDateTime start = OffsetDateTime.ofInstant(Instant.EPOCH, ZoneOffset.UTC);
            OffsetDateTime stop = OffsetDateTime.now(ZoneOffset.UTC);

            DeletePredicateRequest predicate = new DeletePredicateRequest()
                    .start(start)
                    .stop(stop)
                    .predicate(String.format("_measurement=\"%s\"", measurement));

            deleteApi.delete(predicate, influxDBConfig.getInfluxBucket(), influxDBConfig.getInfluxOrg());

            log.info("'{}' 측정값 삭제 완료", measurement);
        } catch (Exception e) {
            log.error("'{}' 삭제 중 오류 발생: {}", measurement, e.getMessage(), e);
        }
    }
}
