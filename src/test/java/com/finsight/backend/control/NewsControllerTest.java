package com.finsight.backend.control;

import com.finsight.backend.config.RootConfig;
import com.finsight.backend.config.WebClientConfig;
import com.finsight.backend.dto.response.KeywordResponseDTO;
import com.finsight.backend.dto.response.NewsByKeywordResponseDTO;
import com.finsight.backend.dto.response.NewsResponseDTO;
import com.finsight.backend.service.NewsService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        RootConfig.class,
        WebClientConfig.class
})
@Transactional
class NewsServiceTest {

    @Autowired
    private NewsService newsService;

    @Test
    @DisplayName("상위 10개 키워드 조회")
    void getTop10KeywordsFromDb() {
        List<KeywordResponseDTO> result = newsService.getTopKeywords();

        System.out.println("--- Top 10 Keywords from DB ---");
        result.forEach(dto ->
                System.out.printf("Keyword: %s, Count: %d%n", dto.getKeyword(), dto.getCount())
        );
    }


    @Test
    @DisplayName("키워드 ID로 뉴스 목록 조회")
    void getNewsByKeywordId_Test() {
        Long keywordId = 2L;

        NewsByKeywordResponseDTO result = newsService.getNewsByKeywordId(keywordId);

        System.out.printf("--- News for Keyword ID: %d ---%n", keywordId);
        result.getNewsList().forEach(news -> {
            System.out.println("Title: " + news.getNewsTitle());
            String publishedAt = news.getNewsPublishedAt() != null ? news.getNewsPublishedAt().toString() : "[unread]";
            System.out.println("Published At: " + publishedAt);
        });
    }

    @Test
    @DisplayName("상품 코드로 뉴스 목록 조회")
    void getNewsByProductCode_Test() {
        // given - DB에 존재하는 실제 상품 코드
        String productCode = "448630"; // 👈 테스트할 상품 코드로 변경하세요.

        // when
        List<NewsResponseDTO> result = newsService.getNewsByProductCode(productCode);

        System.out.printf("--- News for Product Code: %s ---%n", productCode);
        result.forEach(news -> System.out.println("Title: " + news.getNewsTitle()));
    }
}