package com.finsight.backend.domain.vo.news;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewsKeywordVO {
    private String newsId;      // news_id
    private Long keywordId;     // keyword_id
}
