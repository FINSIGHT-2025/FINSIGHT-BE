package com.finsight.backend.control;

import com.finsight.backend.recommend.NewsClickRecommender;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 사용자 뉴스 열람 기반 추천 API 컨트롤러
 */
@RestController
@RequestMapping("/recommendations")
public class RecommendationController {

    private final NewsClickRecommender recommender;

    public RecommendationController(NewsClickRecommender recommender) {
        this.recommender = recommender;
    }

    /**
     * GET /recommendations/{userId}?top=5
     * - 최근 뉴스 열람 로그를 기반으로 추천 상품 ID + 점수 반환
     */
    @GetMapping("/{userId}")
    public ResponseEntity<Map<String, Integer>> recommend(
            @PathVariable String userId,
            @RequestParam(defaultValue = "5") int top
    ) {
        Map<String, Integer> result = recommender.getTopRecommendedProducts(userId, top);
        return ResponseEntity.ok(result);
    }
}
