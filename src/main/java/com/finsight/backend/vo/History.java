package com.finsight.backend.vo;

import com.finsight.backend.dto.request.TradeRequest;

public class History {
    private Long historyId; // UUID or sequence 관리 가능
    private Long holdingsId;
    private String historyTradeType; // buy, sell
    private int historyQuantity;
    private long historyAmount;

    public History() {}

    public History(TradeRequest req, Long holdingsId, String tradeType) {
        this.holdingsId = holdingsId;
        this.historyTradeType = tradeType;
        this.historyQuantity = req.getQuantity();
        this.historyAmount = req.getAmount();
    }

    // Getter/Setter 생략
}
