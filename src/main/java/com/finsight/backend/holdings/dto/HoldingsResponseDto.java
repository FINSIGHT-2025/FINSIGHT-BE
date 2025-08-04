package com.finsight.backend.holdings.dto;

import com.finsight.backend.holdings.vo.HistoryVO;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class HoldingsResponseDto {
    private Long holdingsId;                // 히스토리 조회에 필요
    private String productCategory;          // 상품 유형 (예금, 펀드, ETF 등)
    private double holdingsTotalPrice;       // 총 투자 금액
    private int holdingsTotalQuantity;       // 총 투자 수량
    private String holdingsStatus;            // 보유 상태

    private List<HistoryVO> history;          // 거래 내역 리스트

    private boolean isWatched;  // 찜 여부 (true/false)
}
