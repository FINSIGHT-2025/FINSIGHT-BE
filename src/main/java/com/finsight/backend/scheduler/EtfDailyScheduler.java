package com.finsight.backend.scheduler;

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

    public EtfDailyScheduler(JobLauncher jobLauncher, Job etfDailyJob) {
        this.jobLauncher = jobLauncher;
        this.etfDailyJob = etfDailyJob;
    }

    @Scheduled(cron = "0 15 16 * * *")
    public void launchFundDailyJob() {
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
