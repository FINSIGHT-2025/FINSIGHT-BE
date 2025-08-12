package com.finsight.backend.domain.vo.product;


import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class FAssetAllocationVO {
    private Integer fAssetAllocationId;
    private String fAssetAllocationType;
    private Double fAssetAllocationPer;
    private Double fAssetAllocationValuation;
}
