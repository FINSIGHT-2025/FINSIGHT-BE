package com.finsight.backend.service;

import com.finsight.backend.dto.response.*;
import com.finsight.backend.mapper.EtfMapper;
import com.finsight.backend.mapper.FundMapper;
import com.finsight.backend.mapper.NewsMapper;
import com.finsight.backend.vo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NewsService {

    private final NewsProductSelector newsProductSelector;
    private final NewsMapper newsMapper;
    private final EtfMapper etfMapper;
    private final FundMapper fundMapper;

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

        List<NewsProductVO> candidateProducts = newsMapper.findProductInfoByKeywordId(keywordId);
        List<NewsProductVO> top3Products = newsProductSelector.recommendTop3(candidateProducts);

        /* 수정 필요 */
        List<Object> productDetails = new ArrayList<>();
        for (NewsProductVO info : top3Products) {
            String category = info.getNewsProductCategory();
            String code = info.getProductCode();

            if ("etf".equalsIgnoreCase(category)) {
                NewsEtfDTO etf = etfMapper.findEtfByProductCode(code);
                if (etf != null) {
                    productDetails.add(etf);
                }
            } else if ("fund".equalsIgnoreCase(category)) {
                NewsFundDTO fund = fundMapper.findFundByProductCode(code);
                if (fund != null) {
                    productDetails.add(fund);
                }
            }
        }

        return new NewsByKeywordResponseDTO(newsDtos, productDetails);
        /* ============== */
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
