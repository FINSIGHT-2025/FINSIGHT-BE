package com.finsight.backend.tradeserverwebsocket.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class ETFCodeService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<String> getAllEtfCodes() {
        String url = "http://localhost:3000/etf";

        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                JsonNode root = objectMapper.readTree(response.getBody());
                JsonNode symbolsNode = root.path("symbols");

                List<String> codes = new ArrayList<>();
                if (symbolsNode.isArray()) {
                    for (JsonNode codeNode : symbolsNode) {
                        codes.add(codeNode.asText());
                    }
                    log.debug("ETF 코드 리스트 수신 완료. 수량: {}", codes.size());
                }
                return codes;
            } else {
                log.error("ETF 코드 응답 실패. 상태 코드: {}", response.getStatusCode());
                return Collections.emptyList();
            }

        } catch (Exception e) {
            log.error("ETF 코드 요청 중 오류 발생", e);
            return Collections.emptyList();
        }
    }
}
