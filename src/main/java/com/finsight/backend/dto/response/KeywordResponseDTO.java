package com.finsight.backend.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class KeywordResponseDTO {
    private Long keywordId;
    private String keyword;
    private long positiveCount;
    private long negativeCount;
    private long neutralCount;
    private long totalCount;
}