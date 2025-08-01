package com.finsight.backend.batch;

import com.finsight.backend.config.BatchConfig;
import com.finsight.backend.config.RootConfig;
import com.finsight.backend.config.WebClientConfig;
import com.finsight.backend.service.NewsSaveService;
import com.finsight.backend.service.NewsApiService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        BatchConfig.class,
        WebClientConfig.class,
        RootConfig.class
})
public class NewsSchedulerTest {
    @Autowired
    private NewsApiService newsApiService;

    @Autowired
    private NewsSaveService newsSaveService;

    @Test
    public void testFetchNewsBatch() {
        NewsScheduler scheduler = new NewsScheduler(newsApiService, newsSaveService);
        scheduler.fetchNewsBatch();
    }
}