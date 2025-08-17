package com.finsight.backend.controller.pricehistory;

import com.finsight.backend.dto.response.FundPriceHistoryDto;
import com.finsight.backend.service.product.pricehistory.FundService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/fund")
public class FundController {

    private final FundService fundService;

    @GetMapping("/{code}/history")
    public ResponseEntity<List<FundPriceHistoryDto>> getFundPriceHistory(@PathVariable("code") String productCode) {
        return ResponseEntity.ok(fundService.getFundPriceHistory(productCode));
    }
}
