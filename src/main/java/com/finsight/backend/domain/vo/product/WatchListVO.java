package com.finsight.backend.domain.vo.product;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WatchListVO {
    private Long watchListId;
    private String productCode;
    private String productCategory;
}
