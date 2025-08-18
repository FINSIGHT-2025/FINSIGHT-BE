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
    private Boolean isWatched;

    // 보유량 및 평가액
    private Integer currentHoldings;
    private Double currentValuation;
    
    // 전일 대비 가격 변동 (Fund/ETF)
    private Double previousDayPrice;
    private Double priceChange;
    private Double priceChangePercent;
    private Double totalPriceChange;
    
    // 예금 만기일
    private String maturityDate;
}

