package com.finsight.backend.domain.vo.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WatchVO {
    private Long watchlistId;
    private String userId;
    private String productCode;
    private String productCategory;
}
