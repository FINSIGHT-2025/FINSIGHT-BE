package com.finsight.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
public class FundPriceHistoryDto {
    private LocalDate baseDate;
    private double fundNav;
    private double fundAum;
}
