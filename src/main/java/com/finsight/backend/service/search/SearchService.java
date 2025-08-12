package com.finsight.backend.service.search;

import com.finsight.backend.dto.response.search.SearchSuggestionResponseDTO;

import java.util.List;

public interface SearchService {
    List<SearchSuggestionResponseDTO> getSuggestions(String word);
}
