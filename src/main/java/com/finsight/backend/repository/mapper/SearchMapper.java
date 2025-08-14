package com.finsight.backend.repository.mapper;

import com.finsight.backend.dto.response.search.SearchDepositResponseDTO;
import com.finsight.backend.dto.response.search.SearchEtfResponseDTO;
import com.finsight.backend.dto.response.search.SearchFundResponseDTO;
import com.finsight.backend.dto.response.search.SearchSuggestionResponseDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SearchMapper {

    // 검색 제안
    List<SearchSuggestionResponseDTO> findDepositNameByWord(@Param("word") String word);
    List<SearchSuggestionResponseDTO> findFundNameByWord(@Param("word") String word);
    List<SearchSuggestionResponseDTO> findEtfNameByWord(@Param("word") String word);

    // 페이지네이션
    List<SearchDepositResponseDTO> findDepositsByWordPaged(
            @Param("word") String word,
            @Param("limit") int limit,
            @Param("offset") int offset
    );

    List<SearchFundResponseDTO> findFundsByWordPaged(
            @Param("word") String word,
            @Param("limit") int limit,
            @Param("offset") int offset
    );

    List<SearchEtfResponseDTO> findEtfsByWordPaged(
            @Param("word") String word,
            @Param("limit") int limit,
            @Param("offset") int offset
    );
}
