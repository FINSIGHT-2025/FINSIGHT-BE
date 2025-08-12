package com.finsight.backend.controller.search;

import com.finsight.backend.dto.response.search.SearchDepositResponseDTO;
import com.finsight.backend.dto.response.search.SearchEtfResponseDTO;
import com.finsight.backend.dto.response.search.SearchFundResponseDTO;
import com.finsight.backend.dto.response.search.SearchSuggestionResponseDTO;
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
    public List<SearchDepositResponseDTO> getDeposits(@RequestParam("word") String word) {
        return searchService.getDeposits(word);
    }

    @GetMapping(params = "category=fund")
    public List<SearchFundResponseDTO> getFunds(@RequestParam("word") String word) {
        return searchService.getFunds(word);
    }

    @GetMapping(params = "category=etf")
    public List<SearchEtfResponseDTO> getEtfs(@RequestParam("word") String word) {
        return searchService.getEtfs(word);
    }

    @GetMapping("/suggestions")
    public List<SearchSuggestionResponseDTO> getSuggestions(@RequestParam("word") String word) {
        return searchService.getSuggestions(word);
    }

}
