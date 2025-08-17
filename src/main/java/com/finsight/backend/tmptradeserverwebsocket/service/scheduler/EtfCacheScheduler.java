package com.finsight.backend.tmptradeserverwebsocket.service.scheduler;

import com.finsight.backend.tmptradeserverwebsocket.service.EtfCacheUpdateService;
import com.finsight.backend.tmptradeserverwebsocket.service.EtfCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EtfCacheScheduler {

    private final EtfCodeService etfCodeService;
    private final EtfCacheUpdateService etfCacheUpdateService;

    @Scheduled(cron = "0 0 1 * * *") // 매일 오전 1시
    public void updateAllEtfHistoricalPrices() {
        List<String> etfCodes = etfCodeService.getAllEtfCodes();
        for (String code : etfCodes) {
            etfCacheUpdateService.updateHistoricalPrices(code);
        }
    }
}
