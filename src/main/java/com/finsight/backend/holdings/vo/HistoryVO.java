package com.finsight.backend.holdings.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class HistoryVO {
    private Long historyId;
    private Long holdingsId;
    private String historyTradeType;
    private LocalDateTime historyTradeDate;
    private int historyQuantity;
    private Long historyAmount;
}
