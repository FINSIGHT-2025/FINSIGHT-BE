package com.finsight.backend.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewsVO {
    private String newsId;                 // news_id
    private String newsTitle;              // news_title
    private String newsSummary;            // news_summary
    private String newsContentUrl;         // news_content_url
    private LocalDateTime newsPublishedAt; // news_published_at
    private double newsScore;              // news_score
    private NewsSentiment newsSentiment;  // news_sentiment
    private String newsPublisher;          // news_publisher

    // 연관 키워드 리스트
    private List<KeywordVO> keywords;

    // 연관 상품 코드 리스트
    private List<String> productCodes;

    public enum NewsSentiment {
        positive,
        negative,
        neutral
    }
}
