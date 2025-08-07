package com.finsight.backend.detailhodings.vo;

import lombok.Data;

@Data
public class DetailHoldingsVO {
    private Long holdingsId;
    private String userId;
    private String productCode;
    private String productCategory;
    private double holdingsTotalPrice;
    private int holdingsTotalQuantity;
    private String holdingsStatus;       // holding, zero, terminated, expired
}
