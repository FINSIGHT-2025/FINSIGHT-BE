package com.finsight.backend.tradeserverwebsocket.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j; // Slf4j import 추가
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

@Slf4j // Slf4j 어노테이션 추가
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
        Instant threeMonthsAgoRangeEnd = threeMonthsAgo.plusSeconds(86400 * 30); // 3일 후 <- 30일로 잠시 고정
        Instant threeMonthsAgoRangeStart = threeMonthsAgo.minusSeconds(86400 * 3); // 3일 전
        double price3M = historicalPriceService.queryLatestNavInRange(etfCode, threeMonthsAgoRangeStart, threeMonthsAgoRangeEnd, "etf_nav", "etf_nav");

        /*
        if (pricePrevDay == 0.0) {
            log.warn("[과거 데이터 조회 실패] etfCode: '{}'의 전일 종가(pricePrevDay)를 DB에서 찾을 수 없습니다. 데이터 적재 상태를 확인해주세요.", etfCode);
        }
        if (price3M == 0.0) {
            log.warn("[과거 데이터 조회 실패] etfCode: '{}'의 3개월 전 가격(price3MonthsAgo)을 DB에서 찾을 수 없습니다. 데이터 적재 상태를 확인해주세요.", etfCode);
        }
        */

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