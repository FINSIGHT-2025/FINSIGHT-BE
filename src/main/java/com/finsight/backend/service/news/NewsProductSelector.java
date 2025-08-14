package com.finsight.backend.service.news;

import com.finsight.backend.domain.vo.news.NewsProductVO;
import com.finsight.backend.tmptradeserverwebsocket.service.EtfPriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class NewsProductSelector {
    private final EtfPriceService etfPriceService;

    public List<NewsProductVO> recommendTopProduct(List<NewsProductVO> candidates) {
        // ETF와 FUND를 나눠서 각각 5개씩 가져오기
        List<NewsProductVO> topEtfs = candidates.stream()
                .filter(vo -> "etf".equalsIgnoreCase(vo.getNewsProductCategory()))
                .sorted(Comparator.comparingDouble(this::getChangeRate).reversed())
                .limit(5)
                .collect(Collectors.toList());

        List<NewsProductVO> topFunds = candidates.stream()
                .filter(vo -> "fund".equalsIgnoreCase(vo.getNewsProductCategory()))
                .sorted(Comparator.comparingDouble(this::getChangeRate).reversed())
                .limit(5)
                .collect(Collectors.toList());

        // ETF 5개 + FUND 5개 합치기
        return Stream.concat(topEtfs.stream(), topFunds.stream())
                .collect(Collectors.toList());
    }

    /**
     * EtfPriceService를 사용해 전일 대비 변동률 가져오기
     */
    private double getChangeRate(NewsProductVO vo) {
        try {
            String measurement = vo.getNewsProductCategory().equalsIgnoreCase("ETF")
                    ? "etf_price"   // 실제 measurement 이름
                    : "fund_nav";   // 실제 measurement 이름

            return etfPriceService.getPercentChangeFromYesterday(measurement, vo.getProductCode());
        } catch (Exception e) {
            return Double.NEGATIVE_INFINITY; // 오류 시 정렬에서 뒤로 밀리도록
        }
    }
}
