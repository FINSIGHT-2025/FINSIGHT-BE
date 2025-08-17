package com.finsight.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class HoldingDetailDto {
    private String productCode;
    private String productName;
    private String productCompanyName;
    private String productCategory;
    private String productCountry;
    private String productType;

    // 보유량
    private Integer currentHoldings;
    
    // 평가액
    private Double currentValuation;
    
    // 전일 대비 가격 변동 (Fund/ETF만 해당)
    private Double previousDayPrice;
    private Double priceChange;
    private Double priceChangePercent;
    private Double totalPriceChange;
    
    // 예금 전용 필드
    private String maturityDate;
    
    // 공통 필드
    private Boolean isWatched;
}

