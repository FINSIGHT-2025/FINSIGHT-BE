package com.finsight.backend.tradeserverwebsocket.service;

import com.finsight.backend.tradeserverwebsocket.dto.ProductWebSocketDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EtfCalculationService {

    private final EtfCache etfCache;

    // 상품 코드에 해당하는 ETF 정보를 기반으로 WebSocket 전송용 DTO 생성
    public ProductWebSocketDTO calculateDto(String productCode) {
        EtfCache.EtfSnapshot snapshot = etfCache.get(productCode);
        if (snapshot == null) return null;

        double currentPrice = snapshot.getPriceNow();
        long currentVolume = snapshot.getVolumeNow();
        double price3MonthsAgo = snapshot.getPrice3MonthsAgo();
        double pricePrevDay = snapshot.getPricePrevDay();
        double price1sAgo = snapshot.getPrice1sAgo();

        double returnRate3MonthsAgo = calculateReturnRate(currentPrice, price3MonthsAgo);
        double changeFromPrevDay = currentPrice - pricePrevDay;
        double changeRate1s = calculateReturnRate(currentPrice, price1sAgo);

        long timestamp = System.currentTimeMillis();

        return new ProductWebSocketDTO(
                productCode,
                currentPrice,
                currentVolume,
                returnRate3MonthsAgo,
                changeFromPrevDay,
                changeRate1s,
                timestamp
        );
    }

    // 기준 가격이 유효할 경우 수익률(%) 계산
    private double calculateReturnRate(double current, double base) {
        return base > 0 ? (current - base) / base * 100 : 0.0;
    }
}