package com.finsight.backend.tmptradeserverwebsocket.service;

import com.finsight.backend.config.InfluxDBConfig;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.QueryApi;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ETFHistoricalPriceService {

    private final InfluxDBClient influxDBClient;
    private final InfluxDBConfig influxDBConfig;

    public double queryLatestNavInRange(String etfCode, Instant startTime, Instant stopTime, String measurement, String field) {
        QueryApi queryApi = influxDBClient.getQueryApi();

        String flux = String.format(
                "from(bucket: \"%s\") " +
                        "|> range(start: %s, stop: %s) " +
                        "|> filter(fn: (r) => r._measurement == \"%s\" and r.etf_code == \"%s\" and r._field == \"%s\") " +
                        "|> sort(columns: [\"_time\"], desc: true) " +
                        "|> limit(n: 1)",
                influxDBConfig.getInfluxBucket(),
                startTime,
                stopTime,
                measurement,
                etfCode,
                field
        );

        List<FluxTable> tables;
        try {
            tables = queryApi.query(flux, influxDBConfig.getInfluxOrg());
        } catch (Exception e) {
            return 0.0;
        }

        if (tables.isEmpty()) {
            return 0.0;
        }

        for (FluxTable table : tables) {
            if (table.getRecords().isEmpty()) continue;

            for (FluxRecord record : table.getRecords()) {
                Object value = record.getValue();

                if (value instanceof Number) {
                    return ((Number) value).doubleValue();
                }
            }
        }
        return 0.0;
    }
}
