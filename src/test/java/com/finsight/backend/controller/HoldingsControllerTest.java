package com.finsight.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finsight.backend.dto.request.TradeRequest;
import com.finsight.backend.service.TradeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        com.finsight.backend.config.RootConfig.class,
        com.finsight.backend.config.WebConfig.class,
        com.finsight.backend.config.ServletConfig.class
})
@WebAppConfiguration
@Transactional
public class HoldingsControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    private TradeRequest createRequest(String type, int quantity, double amount) {
        TradeRequest request = new TradeRequest();
        request.setUserId("user123");
        request.setProductCode("KR5301A74911");
        request.setProductCategory("fund");
        request.setQuantity(quantity);
        request.setAmount(amount);
        return request;
    }

    @Test
    @DisplayName("✅ 매수 성공")
    void 매수_성공() throws Exception {
        TradeRequest req = createRequest("buy", 3, 300000);

        mockMvc.perform(post("/holdings/purchases")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("✅ 매도 성공")
    void 매도_성공() throws Exception {
        // 선 매수
        TradeRequest buy = createRequest("buy", 3, 300000);
        mockMvc.perform(post("/holdings/purchases")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buy)))
                .andExpect(status().isOk());

        // 후 매도
        TradeRequest sell = createRequest("sell", 2, 200000);
        mockMvc.perform(post("/holdings/sales")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sell)))
                .andExpect(status().isOk());
    }

    @Autowired
    private TradeService tradeService;

    @Test
    @DisplayName("🚫 보유 내역 없는 매도 실패 → assertThrows 검증")
    void 매도_보유없음_실패() {
        // 매도만 하고 보유 내역이 없으므로 실패해야 함
        TradeRequest sell = createRequest("sell", 1, 100000);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            tradeService.processTrade(sell, "sell");
        });

        assertEquals("보유 내역이 없습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("🚫 수량 부족 매도 실패 → assertThrows 검증")
    void 매도_수량부족_실패() {
        // 수량 1만 보유
        TradeRequest buy = createRequest("buy", 1, 100000);
        tradeService.processTrade(buy, "buy");

        // 수량 5 매도 → 실패해야 함
        TradeRequest sell = createRequest("sell", 5, 500000);
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            tradeService.processTrade(sell, "sell");
        });

        assertEquals("보유 수량 또는 금액 부족", exception.getMessage());
    }

}
