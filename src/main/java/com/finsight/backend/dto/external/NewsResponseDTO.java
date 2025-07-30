package com.finsight.backend.dto.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class NewsResponseDTO {
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
        private Esg esg;
        private List<Company> companies;
        private List<Entity> entities;

        @JsonProperty("published_at")
        private String publishedAt;
//        private String body;

        private List<String> keywords;
    }

    @Getter
    @Builder(toBuilder = true)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Esg {
        private EsgCategory category;
        private EsgPolarity polarity;
    }

    @Getter
    @Builder(toBuilder = true)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EsgCategory {
        private String label;
        private String name;
        private Double score;
    }

    @Getter
    @Builder(toBuilder = true)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EsgPolarity {
        private String label;
        private String name;
        private Double score;
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
