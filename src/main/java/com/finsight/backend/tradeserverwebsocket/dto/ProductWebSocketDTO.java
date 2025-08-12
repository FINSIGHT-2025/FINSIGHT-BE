package com.finsight.backend.tradeserverwebsocket.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductWebSocketDTO {
    private String productCode;           // product_code
    private Double currentPrice;          // 현재 시세
    private Long currentVolume;         // 현재 거래량
    private Double return3Months;         // 3개월 전 기준가 수익률 (%)
    private Double changeFromPrevDay;     // 전일 기준가 대비 증감액
    private Double changeRateFromPrevDay; // 전일 기준가 대비 증감률 (%)
    private Double changeRate1s;          // 1초 전 대비 변화율 (%)
    private Long timestamp;               // 데이터 생성 시각 (epoch millis)
}
