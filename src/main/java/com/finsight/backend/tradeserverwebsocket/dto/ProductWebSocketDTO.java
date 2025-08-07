package com.finsight.backend.tradeserverwebsocket.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductWebSocketDTO {
    private String productCode;           // product_code
    private double currentPrice;          // 현재 시세
    private double currentVolume;         // 현재 거래량
    private double return3Months;         // 3개월 전 기준가 수익률 (%)
    private double changeFromPrevDay;     // 전일 기준가 대비 증감액
    private double changeRate1s;          // 1초 전 대비 변화율 (%)
    private long timestamp;               // 데이터 생성 시각 (epoch millis)
}
