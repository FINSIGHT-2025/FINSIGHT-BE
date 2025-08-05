package com.finsight.backend.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TradeRequest {
    private String userId;
    private String productCode;
    private String productCategory; // deposit, fund, etf
    private int quantity;
    private long amount;
}

