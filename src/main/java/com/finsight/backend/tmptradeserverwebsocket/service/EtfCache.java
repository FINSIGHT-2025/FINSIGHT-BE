package com.finsight.backend.tmptradeserverwebsocket.service;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class EtfCache {

    // product_code 별로 상태 저장
    private final Map<String, EtfSnapshot> snapshotMap = new ConcurrentHashMap<>();

    // 가격과 거래량 업데이트 (기존 버전 - timestamp 없음)
    public void update(String productCode, double priceNow, long volumeNow) {
        update(productCode, priceNow, volumeNow, System.currentTimeMillis());
    }

    // 가격, 거래량, 타임스탬프 포함 업데이트
    public void update(String productCode, double priceNow, long volumeNow, long timestamp) {
        snapshotMap.compute(productCode, (key, oldSnapshot) -> {
            if (oldSnapshot == null) {
                // 최초 등록 시 price1sAgo는 priceNow와 동일하게 세팅
                return new EtfSnapshot(productCode, priceNow, priceNow, volumeNow, 0, 0, timestamp);
            } else {
                oldSnapshot.setPrice3MonthsAgo(oldSnapshot.getPriceNow());
                oldSnapshot.setPrice1sAgo(oldSnapshot.getPriceNow());
                oldSnapshot.setPriceNow(priceNow);
                oldSnapshot.setVolumeNow(volumeNow);
                oldSnapshot.setTimestamp(timestamp);
                oldSnapshot.setProductCode(productCode);
                return oldSnapshot;
            }
        });
    }

    public EtfSnapshot get(String productCode) {
        return snapshotMap.get(productCode);
    }

    public Set<String> getAllProductCodes() {
        return snapshotMap.keySet();
    }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EtfSnapshot {
        private String productCode;
        private double priceNow;
        private double price1sAgo;
        private long volumeNow;
        private double price3MonthsAgo;
        private double pricePrevDay;
        private long timestamp;
    }
}
