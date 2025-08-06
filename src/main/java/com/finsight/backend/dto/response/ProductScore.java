package com.finsight.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProductScore {
    private String productCode;
    private String productCategory; // deposit, fund, etf
    private int score;
}

