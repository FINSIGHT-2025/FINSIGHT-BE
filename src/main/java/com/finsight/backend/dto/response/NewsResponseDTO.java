package com.finsight.backend.dto.response;

import com.finsight.backend.domain.enumerate.NewsSentiment;
import com.finsight.backend.domain.vo.news.NewsVO;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class NewsResponseDTO {
    private String newsId;
    private String newsTitle;
    private String newsPublisher;
    private String newsSummary;
    private String newsContentUrl;
    private LocalDateTime newsPublishedAt;

    private Double newsScore;
    private NewsSentiment newsSentiment;

    public static NewsResponseDTO from(NewsVO vo){
        return NewsResponseDTO.builder()
                .newsId(vo.getNewsId())
                .newsTitle(vo.getNewsTitle())
                .newsPublisher(vo.getNewsPublisher())
                .newsSummary(vo.getNewsSummary())
                .newsContentUrl(vo.getNewsContentUrl())
                .newsPublishedAt(vo.getNewsPublishedAt())
                .newsScore(vo.getNewsScore())
                .newsSentiment(vo.getNewsSentiment())
                .build();
    }
}