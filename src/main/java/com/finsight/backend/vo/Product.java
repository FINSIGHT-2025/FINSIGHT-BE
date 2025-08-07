package com.finsight.backend.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class Product {
    private String productCode;
    private String productName;
    private String productCompanyName;
    private Integer productRiskGrade;
}
