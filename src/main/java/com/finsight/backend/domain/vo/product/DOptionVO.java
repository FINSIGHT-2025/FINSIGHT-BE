package com.finsight.backend.domain.vo.product;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class DOptionVO {
    private Integer dOptionId;
    private String dOptionDclsMonth;
    private String dOptionFinCoNo;
    private String dOptionIntrRateType;
    private String dOptionIntrRateTypeNm ;
    private String dOptionSaveTrm;
    private Double dOptionIntrRate;
    private Double dOptionIntrRate2;
}
