package com.finsight.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WatchPreviewItemDto {
    private Long watchListId;
    private String productCategory;
    private Object detail;
}
