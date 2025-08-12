package com.finsight.backend.domain.vo.news;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KeywordVO {
    private Long keywordId;     // keyword_id
    private String keyword;     // keyword
}
