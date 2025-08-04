package com.finsight.backend.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
@ComponentScan(basePackages = {
        "com.finsight.backend.websocket",
        "com.finsight.backend.config"// 웹소켓 클라이언트 포함
})
public class AppConfig {
    // 기타 빈 설정 가능
}
