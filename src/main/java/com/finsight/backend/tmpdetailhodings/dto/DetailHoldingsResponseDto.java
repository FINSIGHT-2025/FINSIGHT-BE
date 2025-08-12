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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DetailHoldingsResponseDto {

    @JsonProperty("holdings_id")
    private Long holdingsId;

    @JsonProperty("product_category")
    private String productCategory;

    @JsonProperty("holdings_total_price")
    private double holdingsTotalPrice;

    @JsonProperty("holdings_total_quantity")
    private int holdingsTotalQuantity;

    @JsonProperty("holdings_status")
    private String holdingsStatus;

    @JsonProperty("history")
    private List<DetailHistoryVO> history;

    @JsonProperty("is_watched")
    private boolean isWatched;
}
