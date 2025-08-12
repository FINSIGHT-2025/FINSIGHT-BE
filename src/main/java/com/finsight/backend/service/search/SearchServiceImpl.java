package com.finsight.backend.service.search;

import com.finsight.backend.dto.response.search.SearchDepositResponseDTO;
import com.finsight.backend.dto.response.search.SearchEtfResponseDTO;
import com.finsight.backend.dto.response.search.SearchFundResponseDTO;
import com.finsight.backend.dto.response.search.SearchSuggestionResponseDTO;
import com.finsight.backend.repository.mapper.SearchMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService{

    private final SearchMapper searchMapper;

    @Override
    public List<SearchSuggestionResponseDTO> getSuggestions(String word) {
        List<SearchSuggestionResponseDTO> result = new ArrayList<>();

        result.addAll(searchMapper.findDepositNameByWord(word));
        result.addAll(searchMapper.findFundNameByWord(word));
        result.addAll(searchMapper.findEtfNameByWord(word));

        return result;
    }

    @Override
    public List<SearchDepositResponseDTO> getDeposits(String word) {
        List<SearchDepositResponseDTO> result = new ArrayList<>();

        result.addAll(searchMapper.findDepositsByWord(word));

        return result;
    }

    @Override
    public List<SearchFundResponseDTO> getFunds(String word) {
        List<SearchFundResponseDTO> result = new ArrayList<>();

        result.addAll(searchMapper.findFundsByWord(word));

        return result;
    }

    @Override
    public List<SearchEtfResponseDTO> getEtfs(String word) {
        List<SearchEtfResponseDTO> result = new ArrayList<>();

        result.addAll(searchMapper.findEtfsByWord(word));

        return result;

    }
}
