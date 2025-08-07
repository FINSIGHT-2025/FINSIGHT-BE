package com.finsight.backend.vo;


import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class FStockHoldings {
    private Integer fStockHoldingsId;
    private String fStockHoldingsName;
    private String fStockHoldingsCountry;
    private Double fStockHoldingsPer;
}
