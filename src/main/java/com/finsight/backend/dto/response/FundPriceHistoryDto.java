package com.finsight.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
public class FundPriceHistoryDto {
    private LocalDate baseDate;        // 기준 날짜
    private double fundNav;            // 펀드 NAV
    private double fundAum;            // 펀드 규모 (AUM)
    private double weeklyReturn;       // 1주 수익률 (%)
    private double monthlyReturn;      // 1달 수익률 (%)
    private double quarterlyReturn;    // 3달 수익률 (%)
}
