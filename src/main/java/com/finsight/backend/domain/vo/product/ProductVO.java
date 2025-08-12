package com.finsight.backend.domain.vo.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class ProductVO {
    private String productCode;
    private String productName;
    private String productCompanyName;
    private Integer productRiskGrade;
}
