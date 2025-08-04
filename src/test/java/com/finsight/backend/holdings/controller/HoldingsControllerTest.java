package com.finsight.backend.holdings.controller;

import com.finsight.backend.holdings.dto.HoldingsResponseDto;
import com.finsight.backend.holdings.service.HoldingsService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class HoldingsControllerTest {

    private MockMvc mockMvc;
    private HoldingsService holdingsService;
    private HoldingsController holdingsController;

    @Before
    public void setup() {
        holdingsService = Mockito.mock(HoldingsService.class);
        holdingsController = new HoldingsController(holdingsService);
        mockMvc = MockMvcBuilders.standaloneSetup(holdingsController).build();
    }

    @Test
    public void testGetHoldings() throws Exception {
        String userId = "qwerasdf";
        String productCode = "ETF105";

        HoldingsResponseDto mockResponse = HoldingsResponseDto.builder()
                .holdingsId(1L)
                .productCategory("ETF")
                .holdingsTotalPrice(103000.0)
                .holdingsTotalQuantity(10)
                .holdingsStatus("holding")
                .history(Collections.emptyList())
                .build();

        when(holdingsService.getHoldingsWithHistory(userId, productCode))
                .thenReturn(mockResponse);

        mockMvc.perform(get("/holdings/{code}", productCode)
                        .param("user_id", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.holdingsId").value(1))
                .andExpect(jsonPath("$.productCategory").value("ETF"))
                .andExpect(jsonPath("$.holdingsTotalPrice").value(103000.0))
                .andExpect(jsonPath("$.holdingsTotalQuantity").value(10))
                .andExpect(jsonPath("$.holdingsStatus").value("holding"))
                .andExpect(jsonPath("$.history").isArray())
                .andExpect(jsonPath("$.history").isEmpty());
    }
}
