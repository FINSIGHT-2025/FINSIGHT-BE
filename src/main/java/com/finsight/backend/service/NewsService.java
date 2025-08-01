package com.finsight.backend.service;

import com.finsight.backend.dto.response.KeywordResponseDTO;
import com.finsight.backend.dto.response.NewsByKeywordResponseDTO;
import com.finsight.backend.dto.response.NewsResponseDTO;
import com.finsight.backend.mapper.NewsMapper;
import com.finsight.backend.vo.KeywordVO;
import com.finsight.backend.vo.NewsVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NewsService {
    private final NewsMapper newsMapper;

    public List<KeywordResponseDTO> getTopKeywords() {
        List<KeywordVO> topKeywords = newsMapper.findTopKeywords();

        return topKeywords.stream()
                .filter(keywordVO -> keywordVO.getKeywordId() != null)
                .map(keywordVO -> {
                    List<NewsVO> newsList = newsMapper.findNewsByKeywordId(keywordVO.getKeywordId());

                    // --- [디버그 코드 추가] ---
                    System.out.println("\n--- [디버그] 키워드: " + keywordVO.getKeyword() + " ---");
                    System.out.println("전체 뉴스 개수 (newsList.size()): " + newsList.size());

                    // 각 뉴스의 sentiment 값을 직접 출력해봅니다.
                    List<String> sentiments = newsList.stream()
                            .map(vo -> vo.getNewsSentiment() != null ? vo.getNewsSentiment().name() : "NULL")
                            .collect(Collectors.toList());
                    System.out.println("포함된 Sentiment 목록: " + sentiments);
                    // --- [디버그 코드 끝] ---

                    long positive = newsList.stream().filter(n -> "positive".equalsIgnoreCase(n.getNewsSentiment() != null ? n.getNewsSentiment().name() : "")).count();
                    long negative = newsList.stream().filter(n -> "negative".equalsIgnoreCase(n.getNewsSentiment() != null ? n.getNewsSentiment().name() : "")).count();
                    long neutral = newsList.stream().filter(n -> "neutral".equalsIgnoreCase(n.getNewsSentiment() != null ? n.getNewsSentiment().name() : "")).count();

                    return KeywordResponseDTO.builder()
                            .keywordId(keywordVO.getKeywordId())
                            .keyword(keywordVO.getKeyword())
                            .positiveCount(positive)
                            .negativeCount(negative)
                            .neutralCount(neutral)
                            .totalCount(positive + negative + neutral)
                            .build();
                })
                .collect(Collectors.toList());
    }
    public NewsByKeywordResponseDTO getNewsByKeywordId(Long keywordId) {
        // 1. 키워드 ID로 뉴스 목록(VO)을 바로 조회
        List<NewsVO> newsVOs = newsMapper.findNewsByKeywordId(keywordId);

        // 2. VO 리스트를 DTO 리스트로 변환
        List<NewsResponseDTO> newsDtos = newsVOs.stream()
                .map(NewsResponseDTO::from)
                .collect(Collectors.toList());

        // 3. DTO에 뉴스 리스트를 담아 반환
        return new NewsByKeywordResponseDTO(newsDtos);
    }

    public List<NewsResponseDTO> getNewsByProductCode(String productCode) {
        // 상품 코드로 뉴스 목록(VO) 조회
        List<NewsVO> newsVOs = newsMapper.findNewsByProductCode(productCode);

        // 조회된 NewsVO 리스트를 NewsResponseDto 리스트로 변환
        return newsVOs.stream()
                .map(NewsResponseDTO::from)
                .collect(Collectors.toList());
    }
}
