package com.finsight.backend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TradeRequest {
    private String userId;
    private String productCode;
    private String productCategory; // deposit, fund, etf
    private int quantity;
    private long amount;
}

