package com.finsight.backend.tradeserverwebsocket.service;

import com.finsight.backend.config.InfluxDBConfig;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.QueryApi;
import com.influxdb.query.FluxTable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

/*
 - 현재 값 조회: getCurrent(measurement, productCode)
 (fund_aum, K1234)
 - 전일 대비 변화량(값): getPriceChangeFromYesterday(measurement, productCode)
 - 전일 대비 변화량(퍼센트): getPercentChangeFromYesterday(measurement, productCode)
 - 3개월 수익률(퍼센트): getPercentChangeFrom3MonthsAgo(measurement, productCode)
*/

@Service
@RequiredArgsConstructor
public class EtfPriceService {

    private final InfluxDBClient influxDBClient;
    private final InfluxDBConfig influxDBConfig;

    Instant yesterDay = LocalDate.now().minusDays(1)
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant();

    public double getCurrent(String measurement, String productCode) {
        return querySingleValue(measurement, measurement, productCode, yesterDay);
    }

    public double getChangeFromYesterday(String measurement, String productCode) {
        Instant today = yesterDay;
        Instant yesterday = today.minusSeconds(86400);

        double todayValue = querySingleValue(measurement, measurement, productCode, today);
        double yesterdayValue = querySingleValue(measurement, measurement, productCode, yesterday);

        return round(todayValue - yesterdayValue);
    }

    public double getPercentChangeFromYesterday(String measurement, String productCode) {
        Instant today = yesterDay;
        Instant yesterday = today.minusSeconds(86400);

        double todayValue = querySingleValue(measurement, measurement, productCode, today);
        double yesterdayValue = querySingleValue(measurement, measurement, productCode, yesterday);

        if (yesterdayValue != 0) {
            return round((todayValue - yesterdayValue) / yesterdayValue * 100);
        }
        return 0.0;
    }

    public double getPercentChangeFrom3MonthsAgo(String measurement, String productCode) {
        Instant today = yesterDay;
        Instant threeMonthsAgo = today.minusSeconds(60L * 60 * 24 * 90);

        double todayValue = querySingleValue(measurement, measurement, productCode, today);
        double pastValue = querySingleValue(measurement, measurement, productCode, threeMonthsAgo);

        if (pastValue != 0) {
            return round((todayValue - pastValue) / pastValue * 100);
        }
        return 0.0;
    }

    private double querySingleValue(String measurement, String field, String productCode, Instant targetTime) {
        QueryApi queryApi = influxDBClient.getQueryApi();

        String flux = String.format(
                "from(bucket: \"%s\") " +
                        "|> range(start: %s, stop: %s) " +
                        "|> filter(fn: (r) => r._measurement == \"%s\" and r.fund_code == \"%s\" and r._field == \"%s\") " +
                        "|> sort(columns: [\"_time\"], desc: true) " +
                        "|> limit(n: 1)",
                influxDBConfig.getInfluxBucket(),
                targetTime.minusSeconds(3600).toString(),
                targetTime.plusSeconds(3600).toString(),
                measurement,
                productCode,
                field
        );

        List<FluxTable> tables = queryApi.query(flux, influxDBConfig.getInfluxOrg());

        return tables.stream()
                .flatMap(t -> t.getRecords().stream())
                .map(record -> record.getValueByKey("_value"))
                .filter(Double.class::isInstance)
                .map(Double.class::cast)
                .findFirst()
                .map(this::round)
                .orElse(0.0);
    }

    private double round(double value) {
        return new BigDecimal(value).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
}
