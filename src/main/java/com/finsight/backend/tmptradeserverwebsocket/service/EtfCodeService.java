package com.finsight.backend.tmptradeserverwebsocket.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EtfCodeService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${tradedata.url}")
    private String tradeDataUrl;

    public List<String> getAllEtfCodes() {
        String url = tradeDataUrl + "/etf";

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
                }
                return codes;
            } else {
                return Collections.emptyList();
            }

        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
}
