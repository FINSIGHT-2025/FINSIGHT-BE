package com.finsight.backend.batch;

import com.finsight.backend.service.InfluxWriteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Slf4j
@Component
public class FundDailyScheduler {

    @Autowired
    private InfluxWriteService influxWriteService;

//    @Scheduled(cron = "0 5 0 * * *")   // 매일 0시 05분
    @Scheduled(cron = "0 20 15 * * *")
    public void scheduleFundWriteDaily() {
        double fund_nav = 42000 + Math.random() * 200;
        double fund_aum = 2000 + Math.random() * 2000;
        influxWriteService.writeFundDaily(fund_nav, fund_aum, Instant.now());

        log.info("Fund Daily 저장 완료");
    }
}
