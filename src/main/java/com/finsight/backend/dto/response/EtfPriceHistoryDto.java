package com.finsight.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EtfPriceHistoryDto {
    private Long timestamp;
    private Double currentPrice;
    private Long currentVolume;
}