package com.finsight.backend.service.fetcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.finsight.backend.dto.external.NewsApiRequestDTO;
import com.finsight.backend.dto.external.NewsApiResponseDTO;
import com.finsight.backend.service.NewsApiService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class FundNewsFetcher implements NewsFetcher {

    private final NewsApiService newsApiService;
    private final ObjectMapper objectMapper;

    @Override
    public AssetType supports() {
        return AssetType.FUND;
    }

    @Override
    public Mono<NewsApiResponseDTO> fetch(String identifier) {

        System.out.println("FundNewsFetcher가 DB에서 받은 원본 stockHoldings: " + identifier);

        List<String> keywords = parseStockHoldings(identifier);
        System.out.println("keywords = " + keywords);

        if (keywords.isEmpty()) {
            System.out.println("추출된 키워드가 없어 API 호출을 중단합니다.");
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

    private List<String> parseStockHoldings(String identifier) {
        if (identifier == null || identifier.isEmpty()) {
            return List.of();
        }

        try {
            final int MAX_KEYWORDS = 2;

            List<Map<String, Object>> list = objectMapper.readValue(
                    identifier,
                    new TypeReference<List<Map<String, Object>>>() {
                    }
            );

            return list.stream()
                    .map(map -> (String) map.get("종목명"))
                    .filter(Objects::nonNull)
                    .limit(MAX_KEYWORDS)
                    .toList();

        } catch (Exception e) {
            log.error("펀드 주식 보유 비중 JSON 파싱 실패", e);
            return List.of();
        }
    }
}