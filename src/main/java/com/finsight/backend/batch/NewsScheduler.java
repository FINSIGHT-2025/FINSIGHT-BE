package com.finsight.backend.batch;

import com.finsight.backend.dto.external.NewsApiRequestDTO;
import com.finsight.backend.dto.external.NewsApiResponseDTO;
import com.finsight.backend.service.NewsApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import com.finsight.backend.service.NewsSaveService;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class NewsScheduler {
    private final NewsApiService newsApiService;
    private final NewsSaveService newsSaveService;

    // 3분마다 실행
//    @Scheduled(fixedRate = 180000)
    public void fetchNewsBatch() {
        log.info("News Batch job started.");

        // 여러 symbols 목록 정의
        List<List<String>> symbolsList = List.of(
                List.of("448630")
        );

        LocalDate dateTo = LocalDate.now();
        LocalDate dateFrom = dateTo.minusDays(7);

        for (List<String> symbols : symbolsList) {
            NewsApiRequestDTO requestDTO = NewsApiRequestDTO.builder()
                    .symbols(symbols)
                    .dateFrom(dateFrom)
                    .dateTo(dateTo)
                    .build();

            try {
                NewsApiResponseDTO response = newsApiService.fetchNews(requestDTO).block();

                if (response != null && response.getData() != null) {
                    for (NewsApiResponseDTO.NewsData newsData : response.getData()) {
                        newsSaveService.saveNews(newsData, symbols.get(0));
                    }
                    log.info("News saved for symbols: {}", symbols);
                } else {
                    log.warn("No news data received for symbols: {}", symbols);
                }
            } catch (Exception e) {
                log.error("Failed to fetch/save news for symbols {}", symbols, e);
            }
        }
        log.info("News Batch job finished.");
    }
}
