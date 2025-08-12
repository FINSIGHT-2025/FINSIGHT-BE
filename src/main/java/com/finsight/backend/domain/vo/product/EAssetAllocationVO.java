package com.finsight.backend.domain.vo.product;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class EAssetAllocationVO {
    private Integer eAssetAllocationId;
    private String eAssetAllocationType;
    private Double eAssetAllocationPer;
    private Double eAssetAllocationValuation;
}
