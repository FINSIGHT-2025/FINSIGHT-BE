package com.finsight.backend.vo;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class EEquityRatio {
    private Integer eEquityRatioId;
    private String eEquityRatioName;
    private String eEquityRatioCountry;
    private Double eEquityRatioPer;
}
