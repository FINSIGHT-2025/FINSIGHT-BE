package com.finsight.backend.tradeserverwebsocket.service.scheduler;

import com.finsight.backend.tradeserverwebsocket.service.ETFCacheUpdateService;
import com.finsight.backend.tradeserverwebsocket.service.ETFCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ETFCacheScheduler {

    private final ETFCodeService etfCodeService;
    private final ETFCacheUpdateService etfCacheUpdateService;

    @Scheduled(cron = "0 0 1 * * *") // 매일 오전 1시
    public void updateAllEtfHistoricalPrices() {
        List<String> etfCodes = etfCodeService.getAllEtfCodes();
        for (String code : etfCodes) {
            etfCacheUpdateService.updateHistoricalPrices(code);
        }
    }
}
