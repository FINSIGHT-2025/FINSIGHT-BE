package com.finsight.backend.tmptradeserverwebsocket.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class EtfCacheUpdateService {

    private final EtfCache etfCache;
    private final ETFHistoricalPriceService historicalPriceService;

    // ETF 코드에 맞는 가격과 거래량을 찾아서 업데이트
    public void updateHistoricalPrices(String etfCode) {
        Instant now = Instant.now();
//        Instant threeMonthsAgo = now.minusSeconds(60L * 60 * 24 * 30 * 3);
        Instant threeMonthsAgo = now.minusSeconds(60L * 60 * 24);
        Instant prevDay = now.minusSeconds(60L * 60 * 24);

        double price3M = historicalPriceService.queryNavAtTime(etfCode, threeMonthsAgo, "etf_nav", "etf_nav");
        double pricePrevDay = historicalPriceService.queryNavAtTime(etfCode, prevDay, "etf_nav", "etf_nav");
//        System.out.println("price3M: " + price3M + ", pricePrevDay: " + pricePrevDay);
        EtfCache.EtfSnapshot snapshot = etfCache.get(etfCode);

        if (snapshot != null) {
            snapshot.setPrice3MonthsAgo(price3M);
            snapshot.setPricePrevDay(pricePrevDay);
        } else {
            etfCache.update(etfCode, 0, 0);
            snapshot = etfCache.get(etfCode);
            if (snapshot != null) {
                snapshot.setPrice3MonthsAgo(price3M);
                snapshot.setPricePrevDay(pricePrevDay);
            }
        }
    }
}
