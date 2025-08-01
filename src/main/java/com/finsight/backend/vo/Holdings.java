package com.finsight.backend.vo;

import com.finsight.backend.dto.request.TradeRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Holdings {
    private Long holdingsId;
    private String userId;
    private String productCode;
    private String productCategory;
    private long holdingsTotalPrice;
    private int holdingsTotalQuantity;
    private String holdingsStatus; // holding, zero, terminated, expired

    // 기본 생성자 & 생성용 생성자
    public Holdings() {}

    public Holdings(TradeRequest req) {
        this.userId = req.getUserId();
        this.productCode = req.getProductCode();
        this.productCategory = req.getProductCategory();
        this.holdingsTotalPrice = req.getAmount();
        this.holdingsTotalQuantity = req.getQuantity();
        this.holdingsStatus = "holding";
    }
}

