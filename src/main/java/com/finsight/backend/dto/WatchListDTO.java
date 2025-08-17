package com.finsight.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WatchListDTO {
    private Long watchlistId;
    private String productCode;
    private String userId;
    private String productCategory;
}
