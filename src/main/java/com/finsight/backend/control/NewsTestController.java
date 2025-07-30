package com.finsight.backend.control;

import com.finsight.backend.dto.external.NewsRequestDTO;
import com.finsight.backend.dto.external.NewsResponseDTO;
import com.finsight.backend.service.NewsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import scala.collection.Seq;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/news-test")
public class NewsTestController {

    private final NewsService newsService;

    @GetMapping("")
    public Mono<NewsResponseDTO> fetchNews(
            @RequestParam List<String> symbols,
            @RequestParam String dateFrom,
            @RequestParam String dateTo
    ) {
        NewsRequestDTO requestDTO = NewsRequestDTO.builder()
                .symbols(symbols)
                .dateFrom(LocalDate.parse(dateFrom))
                .dateTo(LocalDate.parse(dateTo))
                .build();

//        String text = "한국어를 처리하는 예시입니닼ㅋㅋㅋㅋㅋ #한국어";
//
//        // Normalize
//        CharSequence normalized = OpenKoreanTextProcessorJava.normalize(text);
//
//        // Tokenize
//        Seq<KoreanTokenizer.KoreanToken> tokens = OpenKoreanTextProcessorJava.tokenize(normalized);
//
//        // Phrase Extraction
//        List<KoreanPhraseExtractor.KoreanPhrase> phrases = OpenKoreanTextProcessorJava.extractPhrases(tokens, true, false);
//        // [한국어(Noun: 0, 3), 처리(Noun: 5, 2), 처리하는 예시(Noun: 5, 7), 예시(Noun: 10, 2), #한국어(Hashtag: 18, 4)]
//
//        log.info("news okt : {}", phrases);
        return newsService.fetchNews(requestDTO);
    }




}