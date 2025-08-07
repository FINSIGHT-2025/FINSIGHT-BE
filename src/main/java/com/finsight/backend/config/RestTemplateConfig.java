package com.finsight.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        // RestTemplate은 Spring에서 간단하게 HTTP 요청을 보내고 응답을 받기 위한 클라이언트
        // ex) String result = restTemplate.getForObject("https://api.example.com/data", String.class);
        return new RestTemplate();
    }
}
