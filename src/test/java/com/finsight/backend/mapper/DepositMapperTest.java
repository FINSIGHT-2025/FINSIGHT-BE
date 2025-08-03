package com.finsight.backend.mapper;

import com.finsight.backend.config.MongoConfig;
import com.finsight.backend.config.RootConfig;
import com.finsight.backend.config.ServletConfig;
import com.finsight.backend.config.WebConfig;
import com.finsight.backend.vo.Deposit;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;


@Slf4j
@ExtendWith({SpringExtension.class})
@ContextConfiguration(classes = {RootConfig.class, MongoConfig.class})
class DepositMapperTest {
    @Autowired
    private DepositMapper depositMapper;

    @Test
    void findDepositByCode() {
        Deposit deposit = depositMapper.findDepositByCode("WR0001E");
        Assertions.assertEquals("iTouch\n" + "우리예금", deposit.getProductName());
    }
}