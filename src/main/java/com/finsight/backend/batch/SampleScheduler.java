package com.finsight.backend.batch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SampleScheduler {

    @Scheduled(fixedRate = 30000)
    public void printMessage() {
        log.info("스케줄링 테스트용 (다인) ----- 30초마다 실행되는 작업입니다!");
    }
}