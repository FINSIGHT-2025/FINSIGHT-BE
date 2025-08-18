package com.finsight.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class ProductByWatchDto {
    private String productCode;
    private String productName;
    private String productCompanyName;
    private Boolean userOwns;
    private Boolean isPopularInUserGroup;
    private Integer productRiskGrade;
}
