package com.finsight.backend.domain.vo.user;

import com.finsight.backend.dto.request.TradeRequest;
import java.math.BigDecimal;

public class HistoryVO {
    private Long historyId; // UUID or sequence 관리 가능
    private Long holdingsId;
    private String historyTradeType; // buy, sell, deposit
    private String historyTradeDate; // 거래일
    private int historyQuantity;
    private BigDecimal historyAmount;
    private Integer contractMonths; // 약정 개월 수

    public HistoryVO() {}

    public HistoryVO(TradeRequest req, Long holdingsId, String tradeType) {
        this.holdingsId = holdingsId;
        this.historyTradeType = tradeType;
        this.historyTradeDate = java.time.LocalDateTime.now().toString(); // 현재 시간을 거래일로 설정
        this.historyQuantity = req.getQuantity();
        this.historyAmount = req.getAmount();
        this.contractMonths = req.getContractMonths();
    }

    // Getter/Setter
    public Long getHistoryId() {
        return historyId;
    }

    public void setHistoryId(Long historyId) {
        this.historyId = historyId;
    }

    public Long getHoldingsId() {
        return holdingsId;
    }

    public void setHoldingsId(Long holdingsId) {
        this.holdingsId = holdingsId;
    }

    public String getHistoryTradeType() {
        return historyTradeType;
    }

    public void setHistoryTradeType(String historyTradeType) {
        this.historyTradeType = historyTradeType;
    }

    public int getHistoryQuantity() {
        return historyQuantity;
    }

    public void setHistoryQuantity(int historyQuantity) {
        this.historyQuantity = historyQuantity;
    }

    public BigDecimal getHistoryAmount() {
        return historyAmount;
    }

    public void setHistoryAmount(BigDecimal historyAmount) {
        this.historyAmount = historyAmount;
    }

    public String getHistoryTradeDate() {
        return historyTradeDate;
    }

    public void setHistoryTradeDate(String historyTradeDate) {
        this.historyTradeDate = historyTradeDate;
    }

    public Integer getContractMonths() {
        return contractMonths;
    }

    public void setContractMonths(Integer contractMonths) {
        this.contractMonths = contractMonths;
    }
}
