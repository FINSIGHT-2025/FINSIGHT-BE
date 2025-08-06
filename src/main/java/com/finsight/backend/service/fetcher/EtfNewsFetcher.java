package com.finsight.backend.service.fetcher;

import com.finsight.backend.dto.external.NewsApiRequestDTO;
import com.finsight.backend.dto.external.NewsApiResponseDTO;
import com.finsight.backend.service.NewsApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class EtfNewsFetcher implements NewsFetcher {

    private final NewsApiService newsApiService;

    @Override
    public AssetType supports() {
        return AssetType.ETF;
    }

    @Override
    public Mono<NewsApiResponseDTO> fetch(String identifier) {
        String symbols = identifier;
        LocalDate to = LocalDate.now();
        LocalDate from = to.minusDays(7);

        NewsApiRequestDTO requestDTO = NewsApiRequestDTO.builder()
                .symbols(symbols)
                .dateFrom(from)
                .dateTo(to)
                .build();
        return newsApiService.fetchNews(requestDTO);
    }
}