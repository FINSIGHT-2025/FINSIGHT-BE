package com.finsight.backend.mapper;

import com.finsight.backend.config.*;
import com.finsight.backend.vo.DOption;
import com.finsight.backend.vo.Deposit;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;


@Slf4j
@ExtendWith({SpringExtension.class})
@ContextConfiguration(classes = {
        RootConfig.class,
        CorsConfig.class,
        MongoConfig.class
})
class DepositMapperTest {
    @Autowired
    private DepositMapper depositMapper;

    @Test
    void findDepositByCode() {
        Deposit deposit = depositMapper.findDepositByCode("1");
        // then
        Assertions.assertNotNull(deposit);
        Assertions.assertEquals("1", deposit.getProductCode());

        List<DOption> options = deposit.getDOption();
        Assertions.assertNotNull(options);

        for (DOption option : options) {
            log.info("[findDepositByCode] DOption : {}",option);
        }
    }

    @Test
    void findDepositListOrderByIntrRate() {
        List<Deposit> depositList = depositMapper.findDepositListOrderByIntrRate();
        Assertions.assertNotNull(depositList);
        for (Deposit deposit : depositList) {
            for (DOption option : deposit.getDOption()) {
                log.info("[findDepositListOrderByIntrRate] {} -> DOption : {}",deposit.getProductCode(), option);
            }
        }
    }

    @Test
    void findDepositListOrderByIntrRate2() {
        List<Deposit> depositList = depositMapper.findDepositListOrderByIntrRate2();
        Assertions.assertNotNull(depositList);
        for (Deposit deposit : depositList) {
            for (DOption option : deposit.getDOption()) {
                log.info("[findDepositListOrderByIntrRate] {} -> DOption : {}",deposit.getProductCode(), option);
            }
        }
    }
}