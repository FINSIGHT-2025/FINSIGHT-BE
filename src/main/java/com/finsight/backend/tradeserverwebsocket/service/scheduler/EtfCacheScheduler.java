package com.finsight.backend.tradeserverwebsocket.service.scheduler;

import com.finsight.backend.tradeserverwebsocket.service.EtfCacheUpdateService;
import com.finsight.backend.tradeserverwebsocket.service.EtfCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EtfCacheScheduler {

    private final EtfCodeService etfCodeService;
    private final EtfCacheUpdateService etfCacheUpdateService;

    // ETF 가격와 거래량을 찾아서 EtfCache 업데이트
    @Scheduled(cron = "0 0 1 * * *") // 매일 오전 1시
    public void updateAllEtfHistoricalPrices() {
        List<String> etfCodes = etfCodeService.getAllEtfCodes();
        for (String code : etfCodes) {
            etfCacheUpdateService.updateHistoricalPrices(code);
        }
    }
}
