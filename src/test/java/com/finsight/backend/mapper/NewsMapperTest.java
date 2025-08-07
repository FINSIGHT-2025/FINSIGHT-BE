package com.finsight.backend.mapper;

import com.finsight.backend.config.*;
import com.finsight.backend.vo.NewsVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@Slf4j
@ExtendWith({SpringExtension.class})
@ContextConfiguration(classes = {
        RootConfig.class,
        MongoConfig.class,
        GcpConfig.class,
        InfluxDBConfig.class,
        BatchConfig.class
})
class NewsMapperTest {

    @Autowired
    private NewsMapper newsMapper;

    @BeforeEach
    void setUp(){
        NewsVO news = new NewsVO(
                "N001",
                "Test1",
                "test1",
                "test1",
                LocalDateTime.now(),
                71.6,
                NewsVO.NewsSentiment.positive,
                "test1",
                new ArrayList<>(),
                new ArrayList<>()
        );
        newsMapper.insertNewsProduct("N001", "K55101EI1739", "fund");
    }

    @Test
    void findNewsSentimentByProductCode() {
        List<String> newsSentimentList = newsMapper.findNewsSentimentByProductCode("K55101EI1739");
        log.info("newsSentimentList : {}", newsSentimentList);
    }
}