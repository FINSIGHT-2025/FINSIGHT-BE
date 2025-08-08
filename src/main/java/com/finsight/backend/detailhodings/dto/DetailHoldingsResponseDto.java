package com.finsight.backend.detailhodings.dto;

import com.finsight.backend.detailhodings.vo.DetailHistoryVO;
import lombok.*;

import java.util.List;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor // 기본 생성자 명시
@AllArgsConstructor // 모든 필드 생성자
public class DetailHoldingsResponseDto {
    private Long holdingsId;
    private String productCategory;
    private double holdingsTotalPrice;
    private int holdingsTotalQuantity;
    private String holdingsStatus;
    private List<DetailHistoryVO> history;
    private boolean isWatched;
}
