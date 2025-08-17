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
    // 총 평가액 및 메시지
    private Double totalValuation;
    private String message;
    
    // 투자 유형별 분류
    private Double timeDeposit;
    private Double domesticInvestment;
    private Double foreignInvestment;
    
    // 보유 상품 목록
    private List<HoldingDetailDto> fundHoldings;
    private List<HoldingDetailDto> etfHoldings;
    private List<HoldingDetailDto> depositHoldings;
}

