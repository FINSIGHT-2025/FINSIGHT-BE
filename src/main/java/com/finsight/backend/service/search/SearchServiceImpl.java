package com.finsight.backend.service.search;

import com.finsight.backend.dto.response.search.*;
import com.finsight.backend.repository.mapper.SearchMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {

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
    public SearchPageResponseDTO<SearchDepositResponseDTO> getDeposits(String word, int page, int size) {
        int offset = page * size;
        int limit = size + 1;
        List<SearchDepositResponseDTO> rows = searchMapper.findDepositsByWordPaged(word, limit, offset);

        boolean hasNext = rows.size() > size;
        if (hasNext) rows = rows.subList(0, size);

        return new SearchPageResponseDTO<>(rows, page, size, hasNext, null);
    }

    @Override
    public SearchPageResponseDTO<SearchFundResponseDTO> getFunds(String word, int page, int size) {
        int offset = page * size;
        int limit = size + 1;
        List<SearchFundResponseDTO> rows = searchMapper.findFundsByWordPaged(word, limit, offset);

        boolean hasNext = rows.size() > size;
        if (hasNext) rows = rows.subList(0, size);

        return new SearchPageResponseDTO<>(rows, page, size, hasNext, null);
    }

    @Override
    public SearchPageResponseDTO<SearchEtfResponseDTO> getEtfs(String word, int page, int size) {
        int offset = page * size;
        int limit = size + 1;
        List<SearchEtfResponseDTO> rows = searchMapper.findEtfsByWordPaged(word, limit, offset);

        boolean hasNext = rows.size() > size;
        if (hasNext) rows = rows.subList(0, size);

        return new SearchPageResponseDTO<>(rows, page, size, hasNext, null);
    }
}
