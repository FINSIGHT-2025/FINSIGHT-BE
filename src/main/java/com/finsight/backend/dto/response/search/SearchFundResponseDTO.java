package com.finsight.backend.dto.response.search;

import com.finsight.backend.dto.NewsSentimentDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchFundResponseDTO {
    private String productCode;
    private String productCountry;
    private String productType;
    private String productName;
    private Double productRateOfReturn;
    private Integer fundScale;
    private Integer productRiskGrade;
    private NewsSentimentDto newsSentiment;
    private boolean userOwns;
    private boolean userWatches;
    private boolean isPopularInUserGroup;
}
