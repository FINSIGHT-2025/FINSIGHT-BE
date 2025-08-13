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

    private final Map<String, EtfSnapshot> snapshotMap = new ConcurrentHashMap<>();

    public void update(String productCode, double priceNow, long volumeNow) {
        update(productCode, priceNow, volumeNow, System.currentTimeMillis());
    }

    public void update(String productCode, double priceNow, long volumeNow, long timestamp) {
        snapshotMap.compute(productCode, (key, oldSnapshot) -> {
            if (oldSnapshot == null) {
                // 최초 등록 시에는 과거 데이터가 없으므로 0으로 초기화
                return new EtfSnapshot(productCode, priceNow, priceNow, volumeNow, 0, 0, timestamp);
            } else {
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