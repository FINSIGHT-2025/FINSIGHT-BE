package com.finsight.backend.service.fetcher;

import com.finsight.backend.dto.external.NewsApiResponseDTO;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

public interface NewsFetcher {
    AssetType supports();

    Mono<NewsApiResponseDTO> fetch(String identifier);
}
