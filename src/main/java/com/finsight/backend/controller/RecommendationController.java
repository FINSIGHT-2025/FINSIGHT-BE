package com.finsight.backend.controller;

import com.finsight.backend.common.adapter.ProductAdapter;
import com.finsight.backend.common.exception.ErrorCode;
import com.finsight.backend.domain.vo.product.ProductVO;
import com.finsight.backend.dto.response.ProductByFilterDto;
import com.finsight.backend.dto.response.ProductDetailDto;
import com.finsight.backend.service.AuthService;
import com.finsight.backend.service.product.ProductService;
import com.finsight.backend.tmprecommend.NewsClickRecommender;
import com.finsight.backend.security.info.UserPrincipal;
import com.finsight.backend.dto.response.ProductScore;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/recommendations")
public class RecommendationController {

    private final ProductAdapter productAdapter;
    private final NewsClickRecommender recommender;
    private final AuthService authService;
    private final ProductService productService;

    @GetMapping("")
    public ResponseEntity<List<ProductScore>> recommend(
            @RequestParam(defaultValue = "5") int top
    ) {
        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = principal.getUserId();

        List<ProductScore> result = recommender.getTopRecommendedProducts(userId, top);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{category}/{code}")
    public ResponseEntity<?> findDetailProduct(HttpServletRequest request,
                                               @PathVariable("category") String category,
                                               @PathVariable("code") String productCode){
        Class<? extends ProductVO> productType = productAdapter.category(category);
        String userId = authService.isValidToken(request);

        if (productType == null) {
            return ResponseEntity.status(ErrorCode.NOT_PATH_INVALID.getHttpStatus())
                    .body(ErrorCode.NOT_PATH_INVALID.getMessage());
        }

        ProductByFilterDto recommendProductDto = productService.recommendProduct(productCode, productType, userId);
        return ResponseEntity.ok(recommendProductDto);
    }
}
