package com.finsight.backend.scheduler;

import com.finsight.backend.batch.init.InitJobState;
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
    private final InitJobState initJobState;

    public FundDailyScheduler(JobLauncher jobLauncher, Job fundDailyJob, InitJobState initJobState) {
        this.jobLauncher = jobLauncher;
        this.fundDailyJob = fundDailyJob;
        this.initJobState = initJobState;
    }

    @Scheduled(cron = "00 06 10 * * *")
    public void launchFundDailyJob() {
        if (!initJobState.isFundInitCompleted()) {
            log.warn("[FundDailyJob] 초기화가 완료되지 않아 실행되지 않음");
            return;
        }

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
