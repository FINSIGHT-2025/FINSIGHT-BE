package com.finsight.backend.domain.vo.product;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class EEquityRatioVO {
    private Integer eEquityRatioId;
    private String eEquityRatioName;
    private String eEquityRatioCountry;
    private Double eEquityRatioPer;
}
