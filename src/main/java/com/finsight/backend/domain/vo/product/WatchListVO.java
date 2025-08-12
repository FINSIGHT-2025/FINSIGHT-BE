package com.finsight.backend.domain.vo.product;

import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WatchListVO {
    private Long watchListId;   // 찜한 상품 ID
    private String userId;      // 사용자 ID
    private String productCode; // 상품 코드
    private String productName; // 상품 명


    // 예금
    private String DOptionSaveTrm;   // 저축 기간
    private String DOptionIntrRate;  // 기준 금리
    private String DOptionIntrRate2; // 최고 금리

    // 펀드 & ETF
    private String HoldingsTotalQuantity; // 현재 보유량
    // (양지윤) 전일 대비 가격 관련 변수들을 넣어야함.


}
/*
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
 */