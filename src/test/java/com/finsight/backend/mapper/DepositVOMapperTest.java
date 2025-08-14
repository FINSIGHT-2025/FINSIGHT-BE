package com.finsight.backend.mapper;

import com.finsight.backend.config.*;
import com.finsight.backend.repository.mapper.DepositMapper;
import com.finsight.backend.domain.vo.product.DOptionVO;
import com.finsight.backend.domain.vo.product.DepositVO;
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
class DepositVOMapperTest {
    @Autowired
    private DepositMapper depositMapper;

    @Test
    void findDepositByCode() {
        DepositVO deposit = depositMapper.findDepositByCode("1");
        // then
        Assertions.assertNotNull(deposit);
        Assertions.assertEquals("1", deposit.getProductCode());

        List<DOptionVO> options = deposit.getDOption();
        Assertions.assertNotNull(options);

        for (DOptionVO option : options) {
            log.info("[findDepositByCode] DOption : {}",option);
        }
    }

    @Test
    void findDepositListOrderByIntrRate() {
        List<DepositVO> depositList = depositMapper.findDepositListOrderByIntrRate();
        Assertions.assertNotNull(depositList);
        for (DepositVO deposit : depositList) {
            for (DOptionVO option : deposit.getDOption()) {
                log.info("[findDepositListOrderByIntrRate] {} -> DOption : {}",deposit.getProductCode(), option);
            }
        }
    }

    @Test
    void findDepositListOrderByIntrRate2() {
        List<DepositVO> depositList = depositMapper.findDepositListOrderByIntrRate2();
        Assertions.assertNotNull(depositList);
        for (DepositVO deposit : depositList) {
            for (DOptionVO option : deposit.getDOption()) {
                log.info("[findDepositListOrderByIntrRate] {} -> DOption : {}",deposit.getProductCode(), option);
            }
        }
    }
}