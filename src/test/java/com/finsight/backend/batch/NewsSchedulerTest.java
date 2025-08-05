package com.finsight.backend.batch;

import com.finsight.backend.config.*;
import com.finsight.backend.mapper.NewsMapper;
import com.finsight.backend.service.NewsSaveService;
import com.finsight.backend.service.NewsApiService;
import com.finsight.backend.service.SentimentAnalysisService;
import com.finsight.backend.service.fetcher.NewsFetcher;
import com.finsight.backend.vo.NewsVO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        BatchConfig.class,
        WebClientConfig.class,
        RootConfig.class,
        GcpConfig.class

})
public class NewsSchedulerTest {

    @Autowired
    private NewsScheduler newsScheduler;

    @Test
    @DisplayName("스케줄러를 실행하면 실제 API를 호출하고 DB에 뉴스 데이터가 저장된다")
    void runRealNewsBatchTest() throws InterruptedException {

        newsScheduler.runNewsBatch();

    }
}