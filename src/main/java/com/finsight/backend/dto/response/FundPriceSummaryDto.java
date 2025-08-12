package com.finsight.backend.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FundPriceSummaryDto {

    private double currentNav;

    private double currentAum;

    private double changeFromYesterday;

    private double percentChangeFromYesterday;

    private double percentChangeFrom3MonthsAgo;
}
