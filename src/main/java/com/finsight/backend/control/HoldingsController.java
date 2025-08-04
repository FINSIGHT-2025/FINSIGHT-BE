package com.finsight.backend.control;

import com.finsight.backend.dto.request.TradeRequest;
import com.finsight.backend.service.TradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/holdings")
public class HoldingsController {

    @Autowired
    private TradeService tradeService;

    @PostMapping("/purchases")
    public ResponseEntity<String> purchase(@RequestBody TradeRequest request) {
        tradeService.processTrade(request, "buy");
        return ResponseEntity.ok("매수 완료");
    }

    @PostMapping("/sales")
    public ResponseEntity<String> sell(@RequestBody TradeRequest request) {
        tradeService.processTrade(request, "sell");
        return ResponseEntity.ok("매도 완료");
    }
}

