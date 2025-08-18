package com.finsight.backend.tmptradeserverwebsocket.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

@Slf4j
@Service
@RequiredArgsConstructor
public class EtfCacheUpdateService {

    private final EtfCache etfCache;
    private final ETFHistoricalPriceService historicalPriceService;

    public void updateHistoricalPrices(String etfCode) {
        ZoneId KST = ZoneId.of("Asia/Seoul");

        // 전일 기준가 조회
        Instant prevDayRangeEnd = LocalDate.now(KST).atStartOfDay(KST).toInstant();
        Instant prevDayRangeStart = LocalDate.now(KST).minusDays(7).atStartOfDay(KST).toInstant();
        double pricePrevDay = historicalPriceService.queryLatestNavInRange(etfCode, prevDayRangeStart, prevDayRangeEnd, "etf_nav", "etf_nav");

        // 3개월 전 가격 조회
        Instant threeMonthsAgo = LocalDate.now(KST).minusMonths(3).atStartOfDay(KST).toInstant();
        Instant threeMonthsAgoRangeEnd = threeMonthsAgo.plusSeconds(86400 * 15);
        Instant threeMonthsAgoRangeStart = threeMonthsAgo.minusSeconds(86400 * 15);
        double price3M = historicalPriceService.queryLatestNavInRange(etfCode, threeMonthsAgoRangeStart, threeMonthsAgoRangeEnd, "etf_nav", "etf_nav");

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