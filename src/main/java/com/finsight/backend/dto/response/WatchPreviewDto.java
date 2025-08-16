package com.finsight.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WatchPreviewDto {
    private List<DepositByWatchDto> deposits;
    private List<FundByWatchDto> funds;
    private List<EtfByWatchDto> etfs;
    private int totalCount;
}
