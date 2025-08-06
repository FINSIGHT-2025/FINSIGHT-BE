package com.finsight.backend.detailhodings.controller;

import com.finsight.backend.detailhodings.dto.DetailHoldingsResponseDto;
import com.finsight.backend.detailhodings.service.DetailHoldingsService;
import com.finsight.backend.security.info.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

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


//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//
//@GetMapping("/{product_code}")
//public HoldingsResponseDto getHoldings(
//        @PathVariable("product_code") String productCode,
//        @AuthenticationPrincipal UserPrincipal principal
//) {
//    String userId = principal.getUserId();
//
//    return holdingsService.getHoldingsWithHistory(userId, productCode);
//}

//@GetMapping("/{product_code}")
//public HoldingsResponseDto getHoldings(@RequestParam("user_id") String userId,
//                                       @PathVariable("product_code") String productCode) {
//    return holdingsService.getHoldingsWithHistory(userId, productCode);
//}