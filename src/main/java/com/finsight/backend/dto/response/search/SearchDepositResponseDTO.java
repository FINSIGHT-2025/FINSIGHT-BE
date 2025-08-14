package com.finsight.backend.dto.response.search;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchDepositResponseDTO {
    private String productCode;
    private String productName;
    private String productCompanyName;
    private String productRiskGrade;
    private Double depositIntrRate;
    private Double depositIntrRate2;
    private boolean userOwns;
    private boolean userWatches;
    private boolean isPopularInUserGroup;
}
