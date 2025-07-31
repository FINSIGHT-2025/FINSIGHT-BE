package com.finsight.backend.batch;

import com.finsight.backend.service.InfluxWriteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Slf4j
@Component
public class EtfRealtimeScheduler {

    @Autowired
    private InfluxWriteService influxWriteService;

//    @Scheduled(fixedRate = 1000) // 1초마다 (TODO: 주석 풀어주기)
    @Scheduled(fixedRate = 30000)
    public void scheduleEtfWriteRealtime() {
        double etf_price = 2000 + Math.random() * 300;
        int etf_volume = 400 + (int)(Math.random() * 100);
        influxWriteService.writeEtfRealtime(etf_price, etf_volume, Instant.now());

        log.info("Fund Realtime 저장 완료");
    }
}
