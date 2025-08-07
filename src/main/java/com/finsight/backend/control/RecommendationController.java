package com.finsight.backend.control;

import com.finsight.backend.recommend.NewsClickRecommender;
import com.finsight.backend.security.info.UserPrincipal;
import com.finsight.backend.dto.response.ProductScore;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recommendations")
public class RecommendationController {

    private final NewsClickRecommender recommender;

    public RecommendationController(NewsClickRecommender recommender) {
        this.recommender = recommender;
    }

    @GetMapping("")
    public ResponseEntity<List<ProductScore>> recommend(
            @RequestParam(defaultValue = "5") int top
    ) {
        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = principal.getUserId();

        List<ProductScore> result = recommender.getTopRecommendedProducts(userId, top);
        return ResponseEntity.ok(result);
    }
}
