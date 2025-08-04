package com.finsight.backend.config;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InfluxDBConfig {

    private static final String INFLUX_URL = "http://localhost:8087";
    private static final String INFLUX_TOKEN = "VXD0AjiHfoL6J6_tkksQxmJUxreq-bEgK7t4S6i3ZULEAMTxo3XJcVUHyvvEqv-QkUwIN0I_GRsAjFGEdPsrJA==";
    private static final String INFLUX_ORG = "finsight";
    private static final String INFLUX_BUCKET = "finsight-data";

    @Bean
    public InfluxDBClient influxDBClient() {
        return InfluxDBClientFactory.create(
                INFLUX_URL, INFLUX_TOKEN.toCharArray(), INFLUX_ORG, INFLUX_BUCKET
        );
    }
}
