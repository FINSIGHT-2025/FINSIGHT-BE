package com.finsight.backend.repository.mapper;

import com.finsight.backend.dto.response.search.SearchSuggestionResponseDTO;

import java.util.List;

public interface SearchMapper {
    List<SearchSuggestionResponseDTO> findDepositNameByWord(String word);
    List<SearchSuggestionResponseDTO> findFundNameByWord(String word);
    List<SearchSuggestionResponseDTO> findEtfNameByWord(String word);
}
