package com.finsight.backend.domain.vo.product;


import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class FStockHoldingsVO {
    private Integer fStockHoldingsId;
    private String fStockHoldingsName;
    private String fStockHoldingsCountry;
    private Double fStockHoldingsPer;
}
