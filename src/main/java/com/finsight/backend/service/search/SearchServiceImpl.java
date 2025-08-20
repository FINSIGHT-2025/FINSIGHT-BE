package com.finsight.backend.service.search;

import com.finsight.backend.client.EsProductSearchClient;
import com.finsight.backend.dto.response.search.*;
import com.finsight.backend.repository.mapper.SearchMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {

    private final SearchMapper searchMapper;
    private final EsProductSearchClient es;

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
        try {
            var esPage = es.search("deposit", word, page, size);
            List<SearchDepositResponseDTO> rows = esPage.docs().stream()
                    .map(this::toDepositDTO)
                    .toList();
            boolean hasNext = (long) (page + 1) * size < esPage.total();
            return new SearchPageResponseDTO<>(rows, page, size, hasNext, null);
        } catch (Exception ex) {
            int offset = page * size;
            int limit = size + 1;
            List<SearchDepositResponseDTO> rows = searchMapper.findDepositsByWordPaged(word, limit, offset);
            boolean hasNext = rows.size() > size;
            if (hasNext) rows = rows.subList(0, size);
            return new SearchPageResponseDTO<>(rows, page, size, hasNext, null);
        }
    }

    @Override
    public SearchPageResponseDTO<SearchFundResponseDTO> getFunds(String word, int page, int size) {
        try {
            var esPage = es.search("fund", word, page, size);
            List<SearchFundResponseDTO> rows = esPage.docs().stream()
                    .map(this::toFundDTO)
                    .toList();
            boolean hasNext = (long) (page + 1) * size < esPage.total();
            return new SearchPageResponseDTO<>(rows, page, size, hasNext, null);
        } catch (Exception ex) {
            int offset = page * size;
            int limit = size + 1;
            List<SearchFundResponseDTO> rows = searchMapper.findFundsByWordPaged(word, limit, offset);
            boolean hasNext = rows.size() > size;
            if (hasNext) rows = rows.subList(0, size);
            return new SearchPageResponseDTO<>(rows, page, size, hasNext, null);
        }
    }

    @Override
    public SearchPageResponseDTO<SearchEtfResponseDTO> getEtfs(String word, int page, int size) {
        try {
            var esPage = es.search("etf", word, page, size);
            List<SearchEtfResponseDTO> rows = esPage.docs().stream()
                    .map(this::toEtfDTO)
                    .toList();
            boolean hasNext = (long) (page + 1) * size < esPage.total();
            return new SearchPageResponseDTO<>(rows, page, size, hasNext, null);
        } catch (Exception ex) {
            int offset = page * size;
            int limit = size + 1;
            List<SearchEtfResponseDTO> rows = searchMapper.findEtfsByWordPaged(word, limit, offset);
            boolean hasNext = rows.size() > size;
            if (hasNext) rows = rows.subList(0, size);
            return new SearchPageResponseDTO<>(rows, page, size, hasNext, null);
        }
    }

    private SearchDepositResponseDTO toDepositDTO(Map<String, Object> src) {
        return new SearchDepositResponseDTO(
                str(src.get("productCode")),
                str(src.get("productName")),
                str(src.get("productCompanyName")),
                intOrNull(src.get("productRiskGrade")),
                false, false, false
        );
    }

    private SearchFundResponseDTO toFundDTO(Map<String, Object> src) {
        return new SearchFundResponseDTO(
                str(src.get("productCode")),
                str(src.get("productCountry")),
                str(src.get("productType")),
                str(src.get("productName")),
                intOrNull(src.get("productRiskGrade")),
                false, false, false
        );
    }

    private SearchEtfResponseDTO toEtfDTO(Map<String, Object> src) {
        return new SearchEtfResponseDTO(
                str(src.get("productCode")),
                str(src.get("productCountry")),
                str(src.get("productType")),
                str(src.get("productName")),
                intOrNull(src.get("productRiskGrade")),
                false, false, false
        );
    }

    private String str(Object v) { return v == null ? null : String.valueOf(v); }
    private Integer intOrNull(Object v) {
        if (v == null) return null;
        if (v instanceof Number n) return n.intValue();
        try { return Integer.parseInt(v.toString()); } catch (Exception e) { return null; }
    }
}
