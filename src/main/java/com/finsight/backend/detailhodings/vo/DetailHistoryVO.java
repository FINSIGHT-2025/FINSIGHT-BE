package com.finsight.backend.detailhodings.vo;

import lombok.Data;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

@Data
public class DetailHistoryVO {
    private Long historyId;
    private Long holdingsId;
    private String historyTradeType;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime historyTradeDate;

    private int historyQuantity;
    private Long historyAmount;
}
