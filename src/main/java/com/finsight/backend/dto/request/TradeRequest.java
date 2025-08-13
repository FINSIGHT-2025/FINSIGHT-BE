package com.finsight.backend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TradeRequest {
    private String userId;
    private String productCode;
    private String productCategory; // deposit, fund, etf
    private int quantity;
    private BigDecimal amount;
    private Integer contractMonths; // 약정 개월 수
}

