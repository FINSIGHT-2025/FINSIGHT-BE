package com.finsight.backend.tmptradeserverwebsocket.service;

import com.finsight.backend.tmptradeserverwebsocket.dto.ProductWebSocketDTO;
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
        double changeRateFromPrevDay = calculateReturnRate(currentPrice, pricePrevDay);
        double changeRate1s = calculateReturnRate(currentPrice, price1sAgo);

        long timestamp = System.currentTimeMillis();

        return new ProductWebSocketDTO(
                productCode,
                currentPrice,
                currentVolume,
                returnRate3MonthsAgo,
                changeFromPrevDay,
                changeRateFromPrevDay,
                changeRate1s,
                timestamp
        );
    }

    private double calculateReturnRate(double current, double base) {
        if (base <= 0) return 0.0;

        double rate = (current - base) / base * 100;
        
        // 소수점 둘째 자리까지 반올림
        return Math.round(rate * 100.0) / 100.0;
    }
}