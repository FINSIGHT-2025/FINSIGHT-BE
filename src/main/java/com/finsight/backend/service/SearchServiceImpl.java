package com.finsight.backend.service;

import com.finsight.backend.dto.response.SearchSuggestionResponseDTO;
import com.finsight.backend.mapper.DepositMapper;
import com.finsight.backend.mapper.EtfMapper;
import com.finsight.backend.mapper.FundMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService{

    private final DepositMapper depositMapper;
    private final FundMapper fundMapper;
    private final EtfMapper etfMapper;

    @Override
    public List<SearchSuggestionResponseDTO> getSuggestions(String word) {
        List<SearchSuggestionResponseDTO> result = new ArrayList<>();

        result.addAll(depositMapper.findDepositNameByWord(word));
        result.addAll(fundMapper.findFundNameByWord(word));
        result.addAll(etfMapper.findEtfNameByWord(word));

        return result;
    }
}
