package com.finsight.backend.control.pricehistory;

import com.finsight.backend.dto.response.EtfPriceHistoryDto;
import com.finsight.backend.service.pricehistory.EtfService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/etf")
public class EtfController {

    private final EtfService etfService;

    @GetMapping("/{code}/history")
    public List<EtfPriceHistoryDto> getEtfPriceHistory(@PathVariable("code") String productCode) {
        return etfService.getThreeMonthsEtfNav(productCode);
    }
}
