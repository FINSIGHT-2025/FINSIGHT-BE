package com.finsight.backend.controller;

import com.finsight.backend.dto.response.SearchSuggestionResponseDTO;
import com.finsight.backend.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @GetMapping("/suggestions")
    public List<SearchSuggestionResponseDTO> getSuggestions(@RequestParam("word") String word) {
        return searchService.getSuggestions(word);
    }
}
