package com.finsight.backend.recommend;

import com.finsight.backend.mongo.UserView;
import com.finsight.backend.mapper.NewsKeywordMapper;
import com.finsight.backend.mapper.KeywordProductMapper;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 사용자 뉴스 열람 기반 실시간 상품 추천 알고리즘
 * - 최근 7일간 뉴스 열람 로그만 활용
 * - 지수 감쇠 함수로 열람 시점에 따른 점수 부여
 * - 뉴스 → 키워드 → 상품 구조로 확장
 */
public class NewsClickRecommender {

    private final MongoTemplate mongoTemplate;
    private final NewsKeywordMapper newsKeywordMapper;
    private final KeywordProductMapper keywordProductMapper;

    public NewsClickRecommender(MongoTemplate mongoTemplate,
                                NewsKeywordMapper newsKeywordMapper,
                                KeywordProductMapper keywordProductMapper) {
        this.mongoTemplate = mongoTemplate;
        this.newsKeywordMapper = newsKeywordMapper;
        this.keywordProductMapper = keywordProductMapper;
    }

    /**
     * 추천 상품 목록을 점수 기준으로 상위 N개 반환
     */
    public Map<String, Integer> getTopRecommendedProducts(String userId, int topN) {
        Date sevenDaysAgo = Date.from(Instant.now().minus(7, ChronoUnit.DAYS));

        // 1. 최근 7일 이내 뉴스 열람 로그 조회
        Query query = new Query(Criteria.where("userId").is(userId)
                .and("type").is("news")
                .and("timestamp").gte(sevenDaysAgo));
        List<UserView> logs = mongoTemplate.find(query, UserView.class);

        // 2. 추천 점수 누적 Map
        Map<String, Integer> productScores = new HashMap<>();

        for (UserView view : logs) {
            int weight = calculateExponentialWeight(view.getTimestamp());

            List<String> keywords = newsKeywordMapper.findKeywordsByNewsId(view.getTargetId());
            for (String keyword : keywords) {
                List<String> products = keywordProductMapper.findProductIdsByKeyword(keyword);
                for (String productId : products) {
                    productScores.merge(productId, weight, Integer::sum);
                }
            }
        }

        // 3. 점수 기준 내림차순 정렬 후 상위 N개 추출
        return productScores.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(topN)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (a, b) -> a, LinkedHashMap::new
                ));
    }

    /**
     * 열람 시각으로부터 경과 시간에 따라 지수 감쇠 기반 점수 계산
     */
    private int calculateExponentialWeight(Date timestamp) {
        long secondsAgo = Duration.between(timestamp.toInstant(), Instant.now()).getSeconds();
        double lambda = 0.00001;     // 감쇠율
        double baseScore = 100.0;    // 최대 점수

        double score = baseScore * Math.exp(-lambda * secondsAgo);
        return (int) score; // 소수점 버림 (필요 시 최소 1점 보장도 가능)
    }
}
