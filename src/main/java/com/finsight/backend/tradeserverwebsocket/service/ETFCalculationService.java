package com.finsight.backend.tradeserverwebsocket.service;

import com.finsight.backend.tradeserverwebsocket.dto.ProductWebSocketDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ETFCalculationService {

    private final ETFCache etfCache;

    public ProductWebSocketDto calculateDto(String productCode) {
        ETFCache.ETFSnapshot snapshot = etfCache.get(productCode);

        if (snapshot == null) return null;

        double currentPrice = snapshot.getPriceNow();
        double currentVolume = snapshot.getVolumeNow();
        double price3MonthsAgo = snapshot.getPrice3MonthsAgo();
        double pricePrevDay = snapshot.getPricePrevDay();
        double price1sAgo = snapshot.getPrice1sAgo();

        double return3Months = 0.0;
        if (price3MonthsAgo > 0) {
            return3Months = (currentPrice - price3MonthsAgo) / price3MonthsAgo * 100;
        }

        double changeFromPrevDay = currentPrice - pricePrevDay;

        double changeRate1s = 0.0;
        if (price1sAgo > 0) {
            changeRate1s = (currentPrice - price1sAgo) / price1sAgo * 100;
        }

        long timestamp = System.currentTimeMillis(); // 현재 시간 밀리초

        return new ProductWebSocketDto(
                productCode,
                currentPrice,
                currentVolume,
                return3Months,
                changeFromPrevDay,
                changeRate1s,
                timestamp
        );
    }

}
