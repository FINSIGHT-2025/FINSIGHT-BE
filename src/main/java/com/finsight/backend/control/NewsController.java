package com.finsight.backend.control;

import com.finsight.backend.dto.external.NewsApiRequestDTO;
import com.finsight.backend.dto.external.NewsApiResponseDTO;
import com.finsight.backend.dto.response.KeywordResponseDTO;
import com.finsight.backend.dto.response.NewsByKeywordResponseDTO;
import com.finsight.backend.dto.response.NewsResponseDTO;
import com.finsight.backend.service.NewsApiService;
import com.finsight.backend.service.NewsService;
import com.finsight.backend.vo.NewsVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.finsight.backend.service.UserViewLogger;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/news")
public class NewsController {

    private final NewsService newsService;
    /**
     * 1. 뉴스 키워드 목록 조회
     */
    @GetMapping("")
    public ResponseEntity<List<KeywordResponseDTO>> getTopKeywords() {

        List<KeywordResponseDTO> keywords = newsService.getTopKeywords();
        return ResponseEntity.ok(keywords);
    }

    /**
     * 2. 뉴스 키워드별 뉴스 및 상품 조회
     */
    @GetMapping("/keyword/{keywordId}")
    public ResponseEntity<NewsByKeywordResponseDTO> getNewsAndProductsByKeyword(
            @PathVariable Long keywordId
    ) {
        // 3. Long 타입의 id를 서비스로 전달
        NewsByKeywordResponseDTO response = newsService.getNewsByKeywordId(keywordId);
        return ResponseEntity.ok(response);
    }

    /**
     * 3. 상품 관련 뉴스 목록 조회
     */
    @GetMapping("/code/{code}")
    public ResponseEntity<List<NewsResponseDTO>> getNewsByProductCode(@PathVariable("code") String productCode) {
        List<NewsResponseDTO> newsDtoList = newsService.getNewsByProductCode(productCode);
        return ResponseEntity.ok(newsDtoList);
    }
  
    private final UserViewLogger logger;

    // public NewsController(UserViewLogger logger) {
    //     this.logger = logger;
    // }

    @PostMapping("/click")
    public ResponseEntity<Void> logClick(
            @RequestParam String newsId
    ) {
        // 🔥 로그인된 사용자 ID 추출
        String userId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        logger.logNewsClick(userId, newsId);
        return ResponseEntity.ok().build();
    }

}
