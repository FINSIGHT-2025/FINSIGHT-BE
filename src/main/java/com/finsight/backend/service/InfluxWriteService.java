package com.finsight.backend.service;

import com.finsight.backend.config.InfluxDBConfig;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.WriteApi;
import com.influxdb.client.domain.DeletePredicateRequest;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Service
public class InfluxWriteService {

    private InfluxDBClient influxDBClient;
    private final InfluxDBConfig influxDBConfig;


    public InfluxWriteService(InfluxDBClient influxDBClient, InfluxDBConfig influxDBConfig) {
        this.influxDBClient = influxDBClient;
        this.influxDBConfig = influxDBConfig;
    }

    public void deleteAllFromMeasurement(String measurement) {
        if (influxDBClient == null) return;

        DeletePredicateRequest request = new DeletePredicateRequest();
        request.setStart(OffsetDateTime.ofInstant(Instant.EPOCH, ZoneOffset.UTC));
        request.setStop(OffsetDateTime.ofInstant(Instant.now(), ZoneOffset.UTC));
        request.setPredicate(String.format("_measurement=\"%s\"", measurement));

        influxDBClient.getDeleteApi().delete(
                request,
                influxDBConfig.getInfluxBucket(),
                influxDBConfig.getInfluxOrg()
        );
    }

    public void writeFundDaily(double fund_nav, double fund_aum, Instant time) {
        Point point = Point.measurement("fund_daily")
                .addField("fund_nav", fund_nav)
                .addField("fund_aum", fund_aum)
                .time(time, WritePrecision.S);

        try (WriteApi writeApi = influxDBClient.getWriteApi()) {
            writeApi.writePoint(point);
        }
    }

    public void writeEtfDaily(double etf_nav, Instant time) {
        Point point = Point.measurement("etf_daily")
                .addField("etf_nav", etf_nav)
                .time(time, WritePrecision.S);

        try (WriteApi writeApi = influxDBClient.getWriteApi()) {
            writeApi.writePoint(point);
        }
    }

    public void writeEtfRealtime(double etf_price, double etf_volume, Instant time) {
        Point point = Point.measurement("etf_realtime")
                .addField("etf_price", etf_price)
                .addField("etf_volume", etf_volume)
                .time(time, WritePrecision.S);

        try (WriteApi writeApi = influxDBClient.getWriteApi()) {
            writeApi.writePoint(point);
        }
    }
}
