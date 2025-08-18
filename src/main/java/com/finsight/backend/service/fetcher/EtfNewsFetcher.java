package com.finsight.backend.service.fetcher;

import com.finsight.backend.dto.external.NewsApiRequestDTO;
import com.finsight.backend.dto.external.NewsApiResponseDTO;
import com.finsight.backend.service.news.NewsApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class EtfNewsFetcher implements NewsFetcher {

    private final NewsApiService newsApiService;

    @Override
    public AssetType supports() {
        return AssetType.ETF;
    }

    @Override
    public Mono<NewsApiResponseDTO> fetch(Object identifier) {
        if (!(identifier instanceof String symbols)) {
            // 잘못된 타입이 들어오면 에러를 반환하거나 비어있는 결과를 반환
            return Mono.error(new IllegalArgumentException("Identifier must be a String for ETF"));
        }

        LocalDate to = LocalDate.now();
        // 임시 수집 뉴스 ( 7일 뉴스 수집 )
        // LocalDate from = to.minusDays(7);
        LocalDate from = to.minusDays(0);

        NewsApiRequestDTO requestDTO = NewsApiRequestDTO.builder()
                .symbols(symbols)
                .dateFrom(from)
                .dateTo(to)
                .build();
        return newsApiService.fetchNews(requestDTO);
    }
}