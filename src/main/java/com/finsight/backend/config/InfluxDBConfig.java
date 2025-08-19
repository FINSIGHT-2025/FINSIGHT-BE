

package com.finsight.backend.config;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.InfluxDBClientOptions;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class InfluxDBConfig {

    @Value("${influxdb.url}")
    private String influxUrl;

    @Value("${influxdb.token}")
    private String influxToken;

    @Value("${influxdb.org}")
    private String influxOrg;

    @Value("${influxdb.bucket}")
    private String influxBucket;

    @Bean
    public InfluxDBClient influxDBClient() {
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(180, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS);

        InfluxDBClientOptions options = InfluxDBClientOptions.builder()
                .url(influxUrl)
                .authenticateToken(influxToken.toCharArray())
                .org(influxOrg)
                .bucket(influxBucket)
                .okHttpClient(okHttpClientBuilder)  // Builder 타입 전달
                .build();

        return InfluxDBClientFactory.create(options);

    }

    public String getInfluxOrg() {
        return influxOrg;
    }

    public String getInfluxBucket() {
        return influxBucket;
    }
}
