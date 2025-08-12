package com.finsight.backend.mapper;

import com.finsight.backend.config.*;
import com.finsight.backend.dto.request.TradeRequest;
import com.finsight.backend.repository.mapper.HoldingsMapper;
import com.finsight.backend.domain.vo.user.HoldingsVO;
import com.mongodb.assertions.Assertions;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

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
        HoldingsVO holdings = new HoldingsVO(
            tradeRequest
        );
        holdingsMapper.insert(holdings);
        Boolean ch = holdingsMapper.existProductByUserIdAndProductCode("gfdsa9497", "K55105BV6755");
        Assertions.assertTrue(ch);
    }
}