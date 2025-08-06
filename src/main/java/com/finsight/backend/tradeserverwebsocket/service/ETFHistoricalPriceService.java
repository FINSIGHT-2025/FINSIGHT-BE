package com.finsight.backend.tradeserverwebsocket.service;

import com.finsight.backend.config.InfluxDBConfig;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.QueryApi;
import com.influxdb.query.FluxTable;
import com.influxdb.query.FluxRecord;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class ETFHistoricalPriceService {

    private final InfluxDBClient influxDBClient;
    private final String bucket;
    private final String org;

    public ETFHistoricalPriceService(InfluxDBClient influxDBClient,
                                     InfluxDBConfig influxDBConfig) {
        this.influxDBClient = influxDBClient;
        this.bucket = influxDBConfig.getInfluxBucket();
        this.org = influxDBConfig.getInfluxOrg();
    }

    /**
     * 특정 ETF 코드와 특정 시점에 가장 가까운 NAV 값을 조회
     *
     * @param etfCode 조회할 코드 (etf_code, fund_code 등)
     * @param targetTime 조회 시점
     * @param measurement 측정 이름 (ex: etf_nav, fund_nav)
     * @param field 조회할 필드 이름 (ex: etf_nav, fund_nav)
     * @return 조회된 NAV 값 (없으면 0.0 리턴)
     */
    public double queryNavAtTime(String etfCode, Instant targetTime, String measurement, String field) {
        QueryApi queryApi = influxDBClient.getQueryApi();

        String flux = String.format(
                "from(bucket: \"%s\") " +
                        "|> range(start: %s, stop: %s) " +
                        "|> filter(fn: (r) => r._measurement == \"%s\" and r.etf_code == \"%s\" and r._field == \"%s\") " +
                        "|> sort(columns: [\"_time\"], desc: true) " +
                        "|> limit(n: 1)",
                bucket,
                targetTime.minusSeconds(3600).toString(),
                targetTime.plusSeconds(3600).toString(),
                measurement,
                etfCode,
                field
        );

        List<FluxTable> tables = queryApi.query(flux, org);

        if (tables.isEmpty()) return 0.0;

        for (FluxTable table : tables) {
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

