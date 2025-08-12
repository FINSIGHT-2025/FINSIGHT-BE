package com.finsight.backend.service.search;

import com.finsight.backend.dto.response.search.SearchDepositResponseDTO;
import com.finsight.backend.dto.response.search.SearchEtfResponseDTO;
import com.finsight.backend.dto.response.search.SearchFundResponseDTO;
import com.finsight.backend.dto.response.search.SearchSuggestionResponseDTO;

import java.util.List;

public interface SearchService {
    List<SearchSuggestionResponseDTO> getSuggestions(String word);

    List<SearchDepositResponseDTO> getDeposits(String word);
    List<SearchFundResponseDTO> getFunds(String word);
    List<SearchEtfResponseDTO> getEtfs(String word);
}
