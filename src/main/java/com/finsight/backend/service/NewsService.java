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
        // 키워드 ID로 뉴스 목록(VO)을 바로 조회
        List<NewsVO> newsVOs = newsMapper.findNewsByKeywordId(keywordId);

        // VO 리스트를 DTO 리스트로 변환
        List<NewsResponseDTO> newsDtos = newsVOs.stream()
                .map(NewsResponseDTO::from)
                .collect(Collectors.toList());

        // 연관 상품 코드 최대 3개 추출
        List<String> productCodes = newsVOs.stream()
                .flatMap(news -> news.getProductCodes().stream())
                .distinct()
                .limit(3)
                .collect(Collectors.toList());

        // DTO에 뉴스 리스트를 담아 반환
        return new NewsByKeywordResponseDTO(newsDtos, productCodes);
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
