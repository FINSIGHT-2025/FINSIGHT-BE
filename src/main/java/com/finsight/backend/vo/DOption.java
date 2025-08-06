package com.finsight.backend.vo;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class DOption {
    private Integer dOptionId;
    private String dOptionDclsMonth;
    private String dOptionFinCoNo;
    private String dOptionIntrRateType;
    private String dOptionIntrRateTypeNm ;
    private String dOptionSaveTrm;
    private Double dOptionIntrRate;
    private Double dOptionIntrRate2;
}
