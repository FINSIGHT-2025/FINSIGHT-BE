package com.finsight.backend.controller.search;

import com.finsight.backend.dto.response.search.*;
import com.finsight.backend.service.search.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @GetMapping(params = "category=deposit")
    public SearchPageResponseDTO<SearchDepositResponseDTO> getDeposits(
            @RequestParam(value = "word") String word,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "4") int size
    ) {
        return searchService.getDeposits(word, page, size);
    }

    @GetMapping(params = "category=fund")
    public SearchPageResponseDTO<SearchFundResponseDTO> getFunds(
            @RequestParam(value = "word") String word,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "4") int size
    ) {
        return searchService.getFunds(word, page, size);
    }

    @GetMapping(params = "category=etf")
    public SearchPageResponseDTO<SearchEtfResponseDTO> getEtfs(
            @RequestParam(value = "word") String word,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "4") int size
    ) {
        return searchService.getEtfs(word, page, size);
    }

    @GetMapping("/suggestions")
    public List<SearchSuggestionResponseDTO> getSuggestions(@RequestParam("word") String word) {
        return searchService.getSuggestions(word);
    }

}
