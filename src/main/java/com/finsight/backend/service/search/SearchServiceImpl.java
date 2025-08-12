package com.finsight.backend.service.search;

import com.finsight.backend.dto.response.search.SearchSuggestionResponseDTO;
import com.finsight.backend.repository.mapper.DepositMapper;
import com.finsight.backend.repository.mapper.EtfMapper;
import com.finsight.backend.repository.mapper.FundMapper;
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
}
