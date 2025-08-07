package com.finsight.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
public class AsyncConfig {
    // @EnableAsync (비동기)를 위해서만 작성
    // 일반적으로 AsyncConfig는 @EnableAsync 사용한다 함
    // RootConfig로 @EnableAsync를 작성 가능
}
