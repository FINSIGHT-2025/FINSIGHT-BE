package com.finsight.backend.controller;

import com.finsight.backend.dto.response.ExchangeResponseDTO;
import com.finsight.backend.service.ExchangeRateService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/exchange")
@RequiredArgsConstructor
public class ExchangeRateController {

    private final ExchangeRateService exchangeRateService;

    @GetMapping("")
    public ResponseEntity<?> getRates() {
        try {
            ExchangeResponseDTO response = exchangeRateService.getProcessedExchangeRates();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("환율 정보 처리 중 오류가 발생했습니다.");
        }
    }
}
