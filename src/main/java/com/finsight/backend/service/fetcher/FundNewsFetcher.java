package com.finsight.backend.service.fetcher;

import com.finsight.backend.dto.external.NewsApiRequestDTO;
import com.finsight.backend.dto.external.NewsApiResponseDTO;
import com.finsight.backend.service.news.NewsApiService;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;


@Slf4j
@Component
@RequiredArgsConstructor
public class FundNewsFetcher implements NewsFetcher {

    private final NewsApiService newsApiService;

    @Override
    public AssetType supports() {
        return AssetType.FUND;
    }

    @Override
    public Mono<NewsApiResponseDTO> fetch(Object identifier) {
        if (!(identifier instanceof List<?> keywordList)) {
            return Mono.error(new IllegalArgumentException("Identifier must be a List<String> for Fund"));
        }

        // Object를 List<String>으로 안전하게 변환
        @SuppressWarnings("unchecked")
        List<String> keywords = (List<String>) keywordList;

        if (keywords.isEmpty()) {
            return Mono.empty();
        }

        LocalDate to = LocalDate.now();
        LocalDate from = to.minusDays(7);

        NewsApiRequestDTO requestDTO = NewsApiRequestDTO.builder()
                .keywords(keywords)
                .dateFrom(from)
                .dateTo(to)
                .build();
        return newsApiService.fetchNews(requestDTO);
    }

}