package com.finsight.backend.controller.pricehistory;

import com.finsight.backend.dto.response.EtfPriceHistoryDto;
import com.finsight.backend.service.product.pricehistory.EtfService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/etf")
public class EtfController {

    private final EtfService etfService;

    @GetMapping("/{code}/history")
    public ResponseEntity<List<EtfPriceHistoryDto>> getEtfPriceHistory(@PathVariable("code") String productCode) {
        return ResponseEntity.ok(etfService.getThreeMonthsEtfNav(productCode));
    }
}
