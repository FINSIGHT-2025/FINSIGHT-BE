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
    private String productCode; // 상품 코드
    private String productName; // 상품명
    private String productCompanyName; // 상품 회사명 (은행명, 금융회사명)
    private String productCategory; // 상품 카테고리 (fund, etf, deposit)
    private String productCountry; // 상품 국가 (domestic, foreign)
    private String productType; // 상품 유형 (equity, bond, mixed, deposit)
    
    // 보유량 관련 (Fund/ETF: 주식 수량, Deposit: 투자금액)
    private Integer currentHoldings; // 현재 보유량 (Fund/ETF: 주식 수량, Deposit: 투자금액)
    
    // 평가액 관련
    private Double currentValuation; // 현재 평가액 (Fund: 현재 시세 기준, Deposit: 투자금액)
    
    // 전일 대비 가격 변동 (Fund/ETF만 해당)
    private Double previousDayPrice; // 전일 기준가
    private Double priceChange; // 전일 대비 가격 변동액
    private Double priceChangePercent; // 전일 대비 가격 변동률 (%)
    private Double totalPriceChange; // 전일 대비 총 평가액 변동액 (가격변동액 × 보유량)
    
    // 예금 전용 필드
    private String maturityDate; // 만기날짜 (예금만 해당)
    
    // 공통 필드
    private Boolean isWatched; // 관심 상품 여부
}

