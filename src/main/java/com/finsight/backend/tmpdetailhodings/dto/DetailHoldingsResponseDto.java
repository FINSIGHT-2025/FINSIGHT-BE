package com.finsight.backend.tmpdetailhodings.dto;

import com.finsight.backend.tmpdetailhodings.vo.DetailHistoryVO;
import lombok.*;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DetailHoldingsResponseDto {

    private Long holdingsId;

    private String productCategory;

    private double holdingsTotalPrice;

    private int holdingsTotalQuantity;

    private String holdingsStatus;

    private List<DetailHistoryVO> history;

    private boolean isWatched;
}
