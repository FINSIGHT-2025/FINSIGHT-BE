package com.finsight.backend.service.search;

import com.finsight.backend.dto.response.search.*;

import java.util.List;

public interface SearchService {
    List<SearchSuggestionResponseDTO> getSuggestions(String word);

    SearchPageResponseDTO<SearchDepositResponseDTO> getDeposits(String word, int page, int size);
    SearchPageResponseDTO<SearchFundResponseDTO> getFunds(String word, int page, int size);
    SearchPageResponseDTO<SearchEtfResponseDTO> getEtfs(String word, int page, int size);
}
