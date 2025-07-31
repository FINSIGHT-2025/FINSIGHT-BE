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
public class FundDailyScheduler {

    private final JobLauncher jobLauncher;
    private final Job fundDailyJob;

    public FundDailyScheduler(JobLauncher jobLauncher, Job fundDailyJob) {
        this.jobLauncher = jobLauncher;
        this.fundDailyJob = fundDailyJob;
    }

    @Scheduled(cron = "0 14 16 * * *")
    public void launchFundDailyJob() {
        try {
            JobParameters params = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();

            jobLauncher.run(fundDailyJob, params);
            log.info("[FundDailyJob] 실행 완료");
        } catch (Exception e) {
            log.error("[FundDailyJob] 실행 중 예외 발생 ", e);
        }
    }
}
