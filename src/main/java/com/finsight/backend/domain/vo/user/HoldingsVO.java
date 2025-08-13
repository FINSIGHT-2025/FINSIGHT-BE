package com.finsight.backend.domain.vo.user;

import com.finsight.backend.dto.request.TradeRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.math.BigDecimal;

@Getter
@Setter
@ToString
public class HoldingsVO {
    private Long holdingsId;
    private String userId;
    private String productCode;
    private String productCategory;
    private BigDecimal holdingsTotalPrice;
    private Integer holdingsTotalQuantity;
    private String holdingsStatus; // holding, zero, terminated, expired

    // 기본 생성자 & 생성용 생성자
    public HoldingsVO() {}

    public HoldingsVO(TradeRequest req) {
        this.userId = req.getUserId();
        this.productCode = req.getProductCode();
        this.productCategory = req.getProductCategory();
        this.holdingsTotalPrice = req.getAmount();
        this.holdingsTotalQuantity = req.getQuantity();
        this.holdingsStatus = "holding";
    }
}

