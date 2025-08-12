package com.finsight.backend.repository.mapper;

import com.finsight.backend.dto.response.search.SearchDepositResponseDTO;
import com.finsight.backend.dto.response.search.SearchEtfResponseDTO;
import com.finsight.backend.dto.response.search.SearchFundResponseDTO;
import com.finsight.backend.dto.response.search.SearchSuggestionResponseDTO;

import java.util.List;

public interface SearchMapper {
    List<SearchSuggestionResponseDTO> findDepositNameByWord(String word);
    List<SearchSuggestionResponseDTO> findFundNameByWord(String word);
    List<SearchSuggestionResponseDTO> findEtfNameByWord(String word);

    List<SearchDepositResponseDTO> findDepositsByWord(String word);
    List<SearchFundResponseDTO> findFundsByWord(String word);
    List<SearchEtfResponseDTO> findEtfsByWord(String word);
}
