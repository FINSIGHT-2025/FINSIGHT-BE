package com.finsight.backend.holdings.vo;

import lombok.Data;

@Data
public class HoldingsVO {
    private Long holdingsId;
    private String userId;
    private String productCode;
    private String productCategory;
    private double holdingsTotalPrice;
    private int holdingsTotalQuantity;
    private String holdingsStatus;       // holding, zero, terminated, expired
}
