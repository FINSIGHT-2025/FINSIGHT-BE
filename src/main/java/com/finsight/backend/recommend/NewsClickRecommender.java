package com.finsight.backend.recommend;

import com.finsight.backend.mongo.UserView;
import com.finsight.backend.mapper.NewsKeywordMapper;
import com.finsight.backend.mapper.KeywordProductMapper;
import com.finsight.backend.dto.response.ProductScore;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

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

    public List<ProductScore> getTopRecommendedProducts(String userId, int topN) {
        Date sevenDaysAgo = Date.from(Instant.now().minus(7, ChronoUnit.DAYS));

        Query query = new Query(Criteria.where("userId").is(userId)
                .and("type").is("news")
                .and("timestamp").gte(sevenDaysAgo));
        List<UserView> logs = mongoTemplate.find(query, UserView.class);

        // Map<productCode, ProductScore>
        Map<String, ProductScore> scoreMap = new HashMap<>();

        for (UserView view : logs) {
            int weight = calculateExponentialWeight(view.getTimestamp());

            List<String> keywords = newsKeywordMapper.findKeywordsByNewsId(view.getTargetId());
            for (String keyword : keywords) {
                List<ProductScore> products = keywordProductMapper.findProductScoresByKeyword(keyword);

                for (ProductScore product : products) {
                    scoreMap.compute(product.getProductCode(), (k, v) -> {
                        if (v == null) {
                            return new ProductScore(product.getProductCode(), product.getProductCategory(), weight);
                        }
                        v.setScore(v.getScore() + weight);
                        return v;
                    });
                }
            }

        }

        return scoreMap.values().stream()
                .sorted(Comparator.comparingInt(ProductScore::getScore).reversed())
                .limit(topN)
                .collect(Collectors.toList());
    }

    private int calculateExponentialWeight(Date timestamp) {
        long secondsAgo = Duration.between(timestamp.toInstant(), Instant.now()).getSeconds();
        double lambda = 0.00001;
        double baseScore = 100.0;

        double score = baseScore * Math.exp(-lambda * secondsAgo);
        return (int) score;
    }
}
