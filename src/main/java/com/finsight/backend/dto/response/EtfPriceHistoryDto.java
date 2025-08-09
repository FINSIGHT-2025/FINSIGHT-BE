package com.finsight.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
public class EtfPriceHistoryDto {
    private LocalDate baseDate;
    private double etfNav;
}
