package com.finsight.backend.batch;

import com.finsight.backend.config.*;
import com.finsight.backend.scheduler.NewsScheduler;
import com.finsight.backend.tmptradeserverwebsocket.service.EtfCacheService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
       /* BatchConfig.class,*/
        WebClientConfig.class,
        RootConfig.class,
        GcpConfig.class,
        NewsSchedulerTest.TestConfig.class
        /*MongoConfig.class*/
})
public class NewsSchedulerTest {
    @Autowired
    private NewsScheduler newsScheduler;

    @Test
    @DisplayName("스케줄러를 실행하면 실제 API를 호출하고 DB에 뉴스 데이터가 저장된다")
    void runRealNewsBatchTest() throws InterruptedException {

        newsScheduler.runNewsBatch();

    }

    @Configuration
    static class TestConfig {

        @Bean
        @Primary
        public EtfCacheService etfCacheService() {
            return Mockito.mock(EtfCacheService.class);
        }
    }
}