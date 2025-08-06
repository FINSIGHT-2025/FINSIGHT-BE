package com.finsight.backend.vo;


import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class FAssetAllocation {
    private Integer fAssetAllocationId;
    private String fAssetAllocationType;
    private Double fAssetAllocationPer;
    private Double fAssetAllocationValuation;
}
