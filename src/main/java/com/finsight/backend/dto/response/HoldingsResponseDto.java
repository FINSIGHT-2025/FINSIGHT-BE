package com.finsight.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class HoldingsResponseDto {
    // 펀드, ETF 평가액 요약
    private Double totalValuation; // 펀드, ETF 평가액 (예: 210,000원)
    private String message; // 안내 메시지 (예: "핀사이트에 가입하고 난 후의 평가액입니다!")
    
    // 투자 모아보기
    private Double timeDeposit; // 정기예금 금액
    private Double domesticInvestment; // 국내 투자 금액
    private Double foreignInvestment; // 해외 투자 금액
    
    // 보유 상품 목록 (도메인별 분리)
    private List<HoldingDetailDto> fundHoldings; // 펀드 보유내역
    private List<HoldingDetailDto> etfHoldings; // ETF 보유내역
    private List<HoldingDetailDto> depositHoldings; // 예금 보유내역
}

