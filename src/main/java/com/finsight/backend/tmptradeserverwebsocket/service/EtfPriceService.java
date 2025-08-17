package com.finsight.backend.tmptradeserverwebsocket.service;

import com.finsight.backend.config.InfluxDBConfig;
import com.finsight.backend.domain.vo.product.PricePointVO;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.QueryApi;
import com.influxdb.query.FluxTable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/*
- getCurrent(measurement, productCode): 가장 최근(어제 기준)의 일별 가격을 조회합니다.
   ex) getCurrent("fund_nav", "K12335")
 - getChangeFromYesterday(measurement, productCode): 전일 대비 가격 변동액을 조회합니다. (어제 가격 - 그저께 가격)
 - getPercentChangeFromYesterday(measurement, productCode): 전일 대비 가격 변동률(%)을 조회합니다.
 - getPercentChangeFrom3MonthsAgo(measurement, productCode): 3개월 전 대비 수익률(%)을 조회합니다.
 - getThreeMonthsPriceHistory(measurement, productCode, tagName): 최근 3개월간의 일별 가격 내역을 조회합니다.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EtfPriceService {

    private final InfluxDBClient influxDBClient;
    private final InfluxDBConfig influxDBConfig;

    // 기준이 되는 시간대
    private static final ZoneId SEOUL_ZONE_ID = ZoneId.of("Asia/Seoul");

    public double getCurrent(String measurement, String productCode) {
        Instant yesterday = LocalDate.now(SEOUL_ZONE_ID).minusDays(1).atStartOfDay(SEOUL_ZONE_ID).toInstant();
        return findLatestValueWithFallback(measurement, measurement, productCode, yesterday);
    }

    public double getChangeFromYesterday(String measurement, String productCode) {
        Instant todayTarget = LocalDate.now(SEOUL_ZONE_ID).minusDays(1).atStartOfDay(SEOUL_ZONE_ID).toInstant();
        Instant yesterdayTarget = todayTarget.minus(1, ChronoUnit.DAYS);

        double todayValue = findLatestValueWithFallback(measurement, measurement, productCode, todayTarget);
        double yesterdayValue = findLatestValueWithFallback(measurement, measurement, productCode, yesterdayTarget);

        if (todayValue == 0.0 || yesterdayValue == 0.0) {
            return 0.0;
        }
        return Math.round((todayValue - yesterdayValue) * 100.0) / 100.0;
    }

    public double getPercentChangeFromYesterday(String measurement, String productCode) {
        Instant todayTarget = LocalDate.now(SEOUL_ZONE_ID).minusDays(1).atStartOfDay(SEOUL_ZONE_ID).toInstant();
        Instant yesterdayTarget = todayTarget.minus(1, ChronoUnit.DAYS);

        double todayValue = findLatestValueWithFallback(measurement, measurement, productCode, todayTarget);
        double yesterdayValue = findLatestValueWithFallback(measurement, measurement, productCode, yesterdayTarget);

        return calculatePercentChange(todayValue, yesterdayValue);
    }

    public double getPercentChangeFrom3MonthsAgo(String measurement, String productCode) {
        Instant todayTarget = LocalDate.now(SEOUL_ZONE_ID).minusDays(1).atStartOfDay(SEOUL_ZONE_ID).toInstant();
        Instant threeMonthsAgoTarget = todayTarget.minus(90, ChronoUnit.DAYS);

        double todayValue = findLatestValueWithFallback(measurement, measurement, productCode, todayTarget);
        double pastValue = findLatestValueWithFallback(measurement, measurement, productCode, threeMonthsAgoTarget);

        return calculatePercentChange(todayValue, pastValue);
    }

    public List<PricePointVO> getThreeMonthsPriceHistory(String measurementAndField, String productCode) {
        QueryApi queryApi = influxDBClient.getQueryApi();

        // measurement에 따라 tag key 자동 선택
        String productCodeTag = measurementAndField.contains("etf") ? "etf_code" : "fund_code";

        Instant now = LocalDate.now(SEOUL_ZONE_ID).minusDays(1).atStartOfDay(SEOUL_ZONE_ID).toInstant();
        Instant start = now.minus(90, ChronoUnit.DAYS);

        // 1단계: 먼저 정확한 90일 범위로 조회 시도
        List<PricePointVO> result = executeThreeMonthsQuery(measurementAndField, productCode, productCodeTag, start, now);
        
        // 2단계: 1단계에서 데이터를 찾지 못했다면 범위를 넓혀서 다시 조회
        if (result.isEmpty()) {
            log.warn("[Fallback] ProductCode: '{}', Measurement: '{}'의 3개월 데이터를 찾지 못해 검색 범위를 확장합니다.", productCode, measurementAndField);
            Instant wideStart = start.minus(7, ChronoUnit.DAYS);
            Instant wideStop = now.plus(7, ChronoUnit.DAYS);
            result = executeThreeMonthsQuery(measurementAndField, productCode, productCodeTag, wideStart, wideStop);
        }

        return result;
    }

    private List<PricePointVO> executeThreeMonthsQuery(String measurementAndField, String productCode, String productCodeTag, Instant start, Instant stop) {
        QueryApi queryApi = influxDBClient.getQueryApi();

        String flux = String.format(
                "from(bucket: \"%s\") " +
                        "|> range(start: %s, stop: %s) " +
                        "|> filter(fn: (r) => r._measurement == \"%s\" and r.%s == \"%s\" and r._field == \"%s\") " +
                        "|> sort(columns: [\"_time\"], desc: false)",
                influxDBConfig.getInfluxBucket(),
                start.toString(),
                stop.toString(),
                measurementAndField,
                productCodeTag,
                productCode,
                measurementAndField
        );

        List<FluxTable> tables = queryApi.query(flux);
        List<PricePointVO> result = new ArrayList<>();

        for (FluxTable table : tables) {
            table.getRecords().forEach(record -> {
                Instant time = record.getTime();
                Object value = record.getValue();
                if (value instanceof Number) {
                    result.add(new PricePointVO(time, Math.round(((Number) value).doubleValue() * 100.0) / 100.0));
                }
            });
        }
        return result;
    }


    private double findLatestValueWithFallback(String measurement, String field, String productCode, Instant targetTime) {
        // 1단계: 먼저 정확히 그날 하루(24시간) 범위 내에서만 조회를 시도합니다.
        Instant narrowStartTime = targetTime;
        Instant narrowStopTime = targetTime.plus(1, ChronoUnit.DAYS);
        double value = executeFluxQuery(measurement, field, productCode, narrowStartTime, narrowStopTime);

        // 2단계: 1단계에서 데이터를 찾지 못했다면(결과가 0.0이면) 범위를 넓혀서 다시 조회합니다.
        if (value == 0.0) {
            log.warn("[Fallback] ProductCode: '{}', TargetDate: '{}'의 데이터를 찾지 못해 검색 범위를 확장합니다.", productCode, targetTime.toString());
            Instant wideStartTime = targetTime.minus(7, ChronoUnit.DAYS);
            Instant wideStopTime = targetTime.plus(7, ChronoUnit.DAYS);
            value = executeFluxQuery(measurement, field, productCode, wideStartTime, wideStopTime);
        }

        return value;
    }

    private double executeFluxQuery(String measurement, String field, String productCode, Instant startTime, Instant stopTime) {
        QueryApi queryApi = influxDBClient.getQueryApi();

        // measurement에 따라 tag key 결정
        String productCodeTag = measurement.contains("etf") ? "etf_code" : "fund_code";

        String flux = String.format(
                "from(bucket: \"%s\") " +
                        "|> range(start: %s, stop: %s) " +
                        "|> filter(fn: (r) => r._measurement == \"%s\" and r.%s == \"%s\" and r._field == \"%s\") " +
                        "|> sort(columns: [\"_time\"], desc: true) " +
                        "|> limit(n: 1)",
                influxDBConfig.getInfluxBucket(),
                startTime.toString(),
                stopTime.toString(),
                measurement,
                productCodeTag,
                productCode,
                field
        );

        List<FluxTable> tables = queryApi.query(flux, influxDBConfig.getInfluxOrg());

        return tables.stream()
                .flatMap(t -> t.getRecords().stream())
                .map(record -> record.getValueByKey("_value"))
                .filter(Number.class::isInstance)
                .map(value -> ((Number) value).doubleValue())
                .findFirst()
                .orElse(0.0);
    }


    private double calculatePercentChange(double currentValue, double pastValue) {
        if (pastValue != 0) {
            return Math.round(((currentValue - pastValue) / pastValue * 100) * 100.0) / 100.0;
        }
        return 0.0;
    }
}