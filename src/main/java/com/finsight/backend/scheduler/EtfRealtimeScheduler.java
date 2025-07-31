package com.finsight.backend.scheduler;

import com.finsight.backend.service.InfluxWriteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Slf4j
@Component
public class EtfRealtimeScheduler {

    private final InfluxWriteService influxWriteService;

    public EtfRealtimeScheduler(InfluxWriteService influxWriteService) {
        this.influxWriteService = influxWriteService;
    }


    @Scheduled(fixedRate = 1000)
    public void writeEtfRealtime() {
        try {
            double price = 2000 + Math.random() * 300;
            int volume = 400 + (int)(Math.random() * 100);

            influxWriteService.writeEtfRealtime(price, volume, Instant.now());
            log.info("[EtfRealtime] 저장 완료");
        } catch (Exception e) {
            log.error("[EtfRealtime] 저장 실패", e);
        }
    }
}
