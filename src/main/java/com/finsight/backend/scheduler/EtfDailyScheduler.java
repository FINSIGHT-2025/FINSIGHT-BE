package com.finsight.backend.scheduler;

import com.finsight.backend.common.AppState;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EtfDailyScheduler {

    private final JobLauncher jobLauncher;
    private final Job etfDailyJob;
    private final AppState appState;

    public EtfDailyScheduler(JobLauncher jobLauncher, Job etfDailyJob, AppState appState) {
        this.jobLauncher = jobLauncher;
        this.etfDailyJob = etfDailyJob;
        this.appState = appState;
    }

    @Scheduled(cron = "30 50 17 * * *")
    public void launchEtfDailyJob() {
        if (!appState.isEtfDailyInitCompleted()) {
            log.warn("[EtfDailyJob] 초기화가 완료되지 않아 실행되지 않음");
            return;
        }

        try {
            JobParameters params = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();

            jobLauncher.run(etfDailyJob, params);
            log.info("[EtfDailyJob] 실행 완료");
        } catch (Exception e) {
            log.error("[EtfDailyJob] 실행 중 예외 발생 ", e);
        }
    }
}
