package com.finsight.backend.controller.pricehistory;

import com.finsight.backend.dto.response.FundPriceHistoryDto;
import com.finsight.backend.service.product.pricehistory.FundService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/fund")
public class FundController {

    private final FundService fundService;

    @GetMapping("/{code}/history")
    public List<FundPriceHistoryDto> getFundPriceHistory(@PathVariable("code") String productCode) {
        return fundService.getFundPriceHistory(productCode);
    }
}
