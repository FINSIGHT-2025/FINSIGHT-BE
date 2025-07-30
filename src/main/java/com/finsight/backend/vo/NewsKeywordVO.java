package com.finsight.backend.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewsKeywordVO {
    private Long newsKeywordId; // news_keyword_id
    private String newsId;      // news_id
    private Long keywordId;     // keyword_id
}
