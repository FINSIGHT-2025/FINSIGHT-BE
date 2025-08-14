package com.finsight.backend.config;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();

        // 추천 설정값을 적용한 HttpClient 생성
        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setMaxConnTotal(200)               // 전체 커넥션 수 제한
                .setMaxConnPerRoute(50)             // 특정 경로(IP, Port) 당 커넥션 수 제한
                .build();

        factory.setHttpClient(httpClient);

        // 추천 설정값을 적용한 타임아웃 설정
        factory.setConnectTimeout(5000);            // 연결 시간 초과 (5초)
        factory.setReadTimeout(5000);               // 응답 시간 초과 (5초)

        return new RestTemplate(factory);
    }
}
