package com.finsight.backend.mapper;

import com.finsight.backend.dto.response.SearchSuggestionResponseDTO;

import java.util.List;

public interface EtfMapper {
    List<SearchSuggestionResponseDTO> findEtfNameByWord(String word);
}
