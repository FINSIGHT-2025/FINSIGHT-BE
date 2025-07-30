package com.finsight.backend.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KeywordVO {
    private Long keywordId;    // keyword_id
    private String keyword;    // keyword
}
