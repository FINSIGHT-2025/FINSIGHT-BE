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

    @JsonProperty("current_nav")
    private double currentNav;

    @JsonProperty("current_aum")
    private double currentAum;

    @JsonProperty("change_from_yesterday")
    private double changeFromYesterday;

    @JsonProperty("percent_change_from_yesterday")
    private double percentChangeFromYesterday;

    @JsonProperty("percent_change_from_3_months_ago")
    private double percentChangeFrom3MonthsAgo;
}
