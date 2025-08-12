package com.finsight.backend.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.finsight.backend.detailhodings.dto.DetailHoldingsResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class ProductDetailDto {
    private String productCode;
    private String productName;
    private String productCompanyName;
    private Integer productRiskGrade;

    private DetailHoldingsResponseDto holdings;
}
