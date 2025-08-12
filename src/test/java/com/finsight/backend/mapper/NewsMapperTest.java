package com.finsight.backend.mapper;

import com.finsight.backend.config.*;
import com.finsight.backend.repository.mapper.NewsMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

@Slf4j
@ExtendWith({SpringExtension.class})
@ContextConfiguration(classes = {
        RootConfig.class,
        WebClientConfig.class,
        WebSocketConfig.class
})
@WebAppConfiguration
class NewsMapperTest {

    @Autowired
    private NewsMapper newsMapper;
    @Test
    void findNewsSentimentByProductCode() {
        List<String> newsSentimentList = newsMapper.findNewsSentimentByProductCode("K55101EI1739");
        log.info("newsSentimentList : {}", newsSentimentList);
    }
}