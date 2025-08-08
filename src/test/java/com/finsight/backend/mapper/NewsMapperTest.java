package com.finsight.backend.mapper;

import com.finsight.backend.config.*;
import com.finsight.backend.security.config.SecurityConfig;
import com.finsight.backend.vo.NewsVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@Slf4j
@ExtendWith({SpringExtension.class})
@ContextConfiguration(classes = {
        RootConfig.class,
        WebClientConfig.class
})
@WebAppConfiguration
class NewsMapperTest {

    @Autowired
    private NewsMapper newsMapper;

    @Test
    void findNewsSentimentByProductCode() {
        List<String> newsSentimentList = newsMapper.findNewsSentimentByProductCode("139240");
        log.info("newsSentimentList : {}", newsSentimentList);
    }
}