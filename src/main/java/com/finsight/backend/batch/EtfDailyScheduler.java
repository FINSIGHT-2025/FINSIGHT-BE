package com.finsight.backend.batch;

import com.finsight.backend.service.InfluxWriteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Slf4j
@Component
public class EtfDailyScheduler {

    @Autowired
    private InfluxWriteService influxWriteService;

//    @Scheduled(cron = "0 5 0 * * *")   // 매일 00시 05분 (TODO: 주석 풀어주기)
    @Scheduled(cron = "0 20 15 * * *")
    public void scheduleEtfWriteDaily() {
        double etf_nav = 2300 + Math.random() * 1000;
        influxWriteService.writeEtfDaily(etf_nav, Instant.now());

        log.info("Fund Daily 저장 완료");
    }
}
