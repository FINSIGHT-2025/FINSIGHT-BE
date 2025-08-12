package com.finsight.backend.scheduler;


import com.finsight.backend.service.news.NewsCoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
public class NewsScheduler {

    private final NewsCoreService newsCoreService;

    /**
     * 뉴스 수집 배치 작업
     */
//    @Scheduled(cron = "0 0 3 * * *")
    public void runNewsBatch() {
        log.info(">>>> News Batch job started.");

        try {
            newsCoreService.processAllAssets();
        } catch (Exception e) {
            log.error("An unexpected error occurred during the news batch job.", e);
        }

        log.info(">>>> News Batch job finished.");
    }
}