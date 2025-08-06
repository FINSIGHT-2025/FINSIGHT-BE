package com.finsight.backend.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
@ComponentScan(basePackages = {
        "com.finsight.backend.tradeserverwebsocket", // 트레이드 서버 웹소켓
        "com.finsight.backend.detailhodings"
})
public class AppConfig {
    // 기타 빈 설정 가능
}
