package com.finsight.backend.service;

import com.finsight.backend.dto.external.NewsApiResponseDTO;
import com.finsight.backend.mapper.NewsMapper;
import com.finsight.backend.vo.KeywordVO;
import com.finsight.backend.vo.NewsVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NewsSaveService {

    private final NewsMapper newsMapper;

    public void saveNews(NewsApiResponseDTO.NewsData data, String productCode) {
        // 뉴스 VO 생성
        NewsVO newsVO = data.toVO(productCode);
        String newsId = newsVO.getNewsId();

        boolean newsExists = newsMapper.existsNewsId(newsId);

        if (!newsExists) {
            newsMapper.insertNews(newsVO);
            log.info("뉴스 저장 완료: {}", newsId);

            // 키워드 및 연관 키워드 저장
            if (newsVO.getKeywords() != null) {
                for (KeywordVO keywordVO : newsVO.getKeywords()) {
                    String keyword = keywordVO.getKeyword();
                    newsMapper.insertKeyword(keyword); // INSERT IGNORE 효과
                    Long keywordId = newsMapper.selectKeywordId(keyword);
                    if (keywordId != null) {
                        newsMapper.insertNewsKeyword(newsId, keywordId);
                    }
                }
            }
        } else {
            log.info("이미 존재하는 뉴스입니다: {}", newsId);
        }

        // 뉴스-상품 연결 저장 (중복 관계 발생 시 무시)
        try {
            newsMapper.insertNewsProduct(newsId, productCode);
        } catch (DuplicateKeyException e) {
            log.info("이미 연결된 뉴스-상품 관계입니다: newsId={}, productCode={}", newsId, productCode);
        }
    }
}