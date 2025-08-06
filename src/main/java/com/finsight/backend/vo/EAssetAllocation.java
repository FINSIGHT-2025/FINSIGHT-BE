package com.finsight.backend.vo;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class EAssetAllocation {
    private Integer eAssetAllocationId;
    private String eAssetAllocationType;
    private Double eAssetAllocationPer;
    private Double eAssetAllocationValuation;
}
