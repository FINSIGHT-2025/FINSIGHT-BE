package com.finsight.backend.service;

import com.finsight.backend.dto.response.SearchSuggestionResponseDTO;

import java.util.List;

public interface SearchService {
    List<SearchSuggestionResponseDTO> getSuggestions(String word);
}
