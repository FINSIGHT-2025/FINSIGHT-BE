package com.finsight.backend.scheduler;

import com.finsight.backend.common.AppState;
import com.finsight.backend.service.InfluxWriteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Slf4j
@Component
public class EtfRealtimeScheduler {

    private final InfluxWriteService influxWriteService;
    private final AppState appState;

    public EtfRealtimeScheduler(InfluxWriteService influxWriteService, AppState appState) {
        this.influxWriteService = influxWriteService;
        this.appState = appState;
    }

    @Scheduled(fixedRate = 1000)
    public void writeEtfRealtime() {
        if (!appState.isEtfRealtimeInitCompleted()) {
            log.warn("[EtfRealtimeJob] 초기화가 완료되지 않아 실행되지 않음");
            return;
        }

        try {
            double price = 2000 + Math.random() * 300;
            double volume = 400 + Math.random() * 100;

            influxWriteService.writeEtfRealtime(price, volume, Instant.now());
            log.info("[EtfRealtime] 저장 완료");
        } catch (Exception e) {
            log.error("[EtfRealtime] 저장 실패", e);
        }
    }
}
