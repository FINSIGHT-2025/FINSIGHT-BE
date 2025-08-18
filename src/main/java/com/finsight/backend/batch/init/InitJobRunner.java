package com.finsight.backend.batch.init;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
/*@DependsOn("influxDBClient")*/
public class InitJobRunner implements ApplicationListener<ContextRefreshedEvent> {

    private final JobLauncher jobLauncher;
    private final Job fundInitJob;
    private final Job etfInitJob;
    private final InitJobState initJobState;

    private boolean alreadyExecuted = false;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (alreadyExecuted) return;
        alreadyExecuted = true;

        try {
            boolean success = true;

            // 펀드 Job 실행
            JobExecution fundJobExecution = jobLauncher.run(fundInitJob,
                    new JobParametersBuilder()
                            .addLong("time", System.currentTimeMillis())
                            .toJobParameters());

            if (fundJobExecution.getStatus() != BatchStatus.COMPLETED) {
                log.error("❌ fundInitJob 실패 - 상태: {}", fundJobExecution.getStatus());
                success = false;
            } else {
                log.info("✅ fundInitJob 실행 완료");
                initJobState.markFundInitCompleted();
            }

            // ETF Job 실행
            JobExecution etfJobExecution = jobLauncher.run(etfInitJob,
                    new JobParametersBuilder()
                            .addLong("time", System.currentTimeMillis() + 1)
                            .toJobParameters());

            if (etfJobExecution.getStatus() != BatchStatus.COMPLETED) {
                log.error("❌ etfInitJob 실패 - 상태: {}", etfJobExecution.getStatus());
                success = false;
            } else {
                log.info("✅ etfInitJob 실행 완료");
                initJobState.markEtfInitCompleted();
            }

            // 최종 로그
            if (success) {
                log.info("✅ 시세 초기화 작업 완료 (펀드 + ETF 시세)");
            } else {
                log.warn("❌ 시세 초기화 작업 실패");
            }

        } catch (Exception e) {
            log.error("❌ 초기화 작업 중 예외 발생", e);
        }
    }

}
