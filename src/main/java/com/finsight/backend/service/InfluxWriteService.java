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

    public void writeFundAum(String fundCode, double fundAum, Instant time) {
        Point point = Point.measurement("fund_aum")
                .addTag("fund_code", fundCode)
                .addField("fund_aum", fundAum)
                .time(time, WritePrecision.S);

        try (WriteApi writeApi = influxDBClient.getWriteApi()) {
            writeApi.writePoint(point);
        }
    }

    public void writeFundNav(String fundCode, double fundNav, Instant time) {
        Point point = Point.measurement("fund_nav")
                .addTag("fund_code", fundCode)
                .addField("fund_nav", fundNav)
                .time(time, WritePrecision.S);

        try (WriteApi writeApi = influxDBClient.getWriteApi()) {
            writeApi.writePoint(point);
        }
    }

    public void writeEtfNav(double etfNav, Instant time) {
        Point point = Point.measurement("etf_nav")
                .addField("etf_nav", etfNav)
                .time(time, WritePrecision.S);

        try (WriteApi writeApi = influxDBClient.getWriteApi()) {
            writeApi.writePoint(point);
        }
    }
}
