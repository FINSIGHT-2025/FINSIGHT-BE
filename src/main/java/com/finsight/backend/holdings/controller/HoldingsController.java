package com.finsight.backend.holdings.controller;

import com.finsight.backend.holdings.dto.HoldingsResponseDto;
import com.finsight.backend.holdings.service.HoldingsService;
import com.finsight.backend.security.info.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@RestController
@RequestMapping("/holdings")
@RequiredArgsConstructor
public class HoldingsController {

    private final HoldingsService holdingsService;

    @GetMapping("/{product_code}")
    public HoldingsResponseDto getHoldings(
            @PathVariable("product_code") String productCode,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        String userId = principal.getUserId();

        return holdingsService.getHoldingsWithHistory(userId, productCode);
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