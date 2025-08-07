package com.finsight.backend.control.detailhodings;

import com.finsight.backend.detailhodings.dto.DetailHoldingsResponseDto;
import com.finsight.backend.detailhodings.service.DetailHoldingsService;
import com.finsight.backend.security.info.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

// 상품세부내역의 보유 내역만 조회하는 컨트롤러
@RestController
@RequestMapping("/holdings/detail")
@RequiredArgsConstructor
public class DetailHoldingsController {

    private final DetailHoldingsService detailHoldingsService;

    @GetMapping("/{product_code}")
    public DetailHoldingsResponseDto getHoldings(
            @PathVariable("product_code") String productCode,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        String userId = principal.getUserId();
        return detailHoldingsService.getHoldingsWithHistory(userId, productCode);
    }
}