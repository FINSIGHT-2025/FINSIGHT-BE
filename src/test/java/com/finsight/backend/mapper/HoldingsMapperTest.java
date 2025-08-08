package com.finsight.backend.mapper;

import com.finsight.backend.config.*;
import com.finsight.backend.dto.request.TradeRequest;
import com.finsight.backend.vo.Holdings;
import com.mongodb.assertions.Assertions;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@ExtendWith({SpringExtension.class})
@ContextConfiguration(classes = {
        RootConfig.class,
        WebClientConfig.class,
        WebSocketConfig.class
})
@WebAppConfiguration
class HoldingsMapperTest {
    @Autowired
    private HoldingsMapper holdingsMapper;

    @Test
    void existProductByUserId() {
        TradeRequest tradeRequest = new TradeRequest(
                "gfdsa9497",
                "K55105BV6755",
                "fund",
                10000,
                12
        );
        Holdings holdings = new Holdings(
            tradeRequest
        );
        holdingsMapper.insert(holdings);
        Boolean ch = holdingsMapper.existProductByUserIdAndProductCode("gfdsa9497", "K55105BV6755");
        Assertions.assertTrue(ch);
    }
}