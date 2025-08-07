package com.finsight.backend.dto.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.finsight.backend.vo.KeywordVO;
import com.finsight.backend.vo.NewsVO;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class NewsApiResponseDTO {
    private Detail detail;

    @JsonProperty("total_items")
    private Integer totalItems;

    @JsonProperty("total_pages")
    private Integer totalPages;
    private Integer page;

    @JsonProperty("page_size")
    private Integer pageSize;
    private List<NewsData> data;

    @Getter
    @Builder(toBuilder = true)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Detail {
        private String message;
        private String code;
        private Boolean ok;
    }

    @Getter
    @Builder(toBuilder = true)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NewsData {
        private String id;
        private List<String> sections;
        private String title;
        private String publisher;
        private String author;
        private String summary;
        private String highlight;
        private Double score;

        @JsonProperty("image_url")
        private String imageUrl;

        @JsonProperty("thumbnail_url")
        private String thumbnailUrl;

        @JsonProperty("content_url")
        private String contentUrl;
        private List<Company> companies;
        private List<Entity> entities;

        @JsonProperty("published_at")
        private String publishedAt;
//        private String body;

        private List<String> keywords;
        private float sentimentScore;

        public NewsVO toVO(String productCode){
            LocalDateTime publishedAtDateTime = null;
            if(this.publishedAt != null){
                publishedAtDateTime = LocalDateTime.parse(this.publishedAt);
            }

            NewsVO.NewsSentiment sentiment;
            if (this.sentimentScore > 0.25) {
                sentiment = NewsVO.NewsSentiment.positive;
            } else if (this.sentimentScore < -0.25) {
                sentiment = NewsVO.NewsSentiment.negative;
            } else {
                sentiment = NewsVO.NewsSentiment.neutral;
            }

            List<KeywordVO> keywordVOList = null;
            if(this.keywords != null){
                keywordVOList = this.keywords.stream()
                        .map(k -> KeywordVO.builder().keyword(k).build())
                        .toList();
            }

            List<String> productCodes = List.of(productCode);

            return NewsVO.builder()
                    .newsId(this.id)
                    .newsTitle(this.title)
                    .newsPublisher(this.publisher)
                    .newsSummary(this.summary)
                    .newsContentUrl(this.contentUrl)
                    .newsPublishedAt(publishedAtDateTime)
                    .newsScore(this.score != null ? this.score : 0.0)
                    .newsSentiment(sentiment)
                    .keywords(keywordVOList)
                    .productCodes(productCodes)
                    .build();
        }
    }

    

    @Getter
    @Builder(toBuilder = true)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Company {
        private String name;
        private String symbol;
        private String exchange;
        private String sentiment;
    }

    @Getter
    @Builder(toBuilder = true)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Entity {
        private String type;
        private String name;
    }
}
