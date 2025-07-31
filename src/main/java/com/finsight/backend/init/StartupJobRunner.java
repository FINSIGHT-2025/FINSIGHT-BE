package com.finsight.backend.init;

import com.finsight.backend.common.AppState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class StartupJobRunner implements ApplicationListener<ContextRefreshedEvent> {

    private final JobLauncher jobLauncher;
    private final Job fundDailyInitJob;
    private final Job etfDailyInitJob;
    private final AppState appState;

    private boolean alreadyExecuted = false;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // 중복 실행 방지 (자식 Context까지 이벤트가 전달될 수 있음)
        if (alreadyExecuted) return;
        alreadyExecuted = true;

        try {
            jobLauncher.run(fundDailyInitJob,
                    new JobParametersBuilder()
                            .addLong("time", System.currentTimeMillis())
                            .toJobParameters());
            log.info("fundDailyInitJob 실행 완료");
            appState.markFundDailyInitCompleted();

            jobLauncher.run(etfDailyInitJob,
                    new JobParametersBuilder()
                            .addLong("time", System.currentTimeMillis() + 1) // JobInstance 충돌 방지
                            .toJobParameters());
            log.info("etfDailyInitJob 실행 완료");
            appState.markEtfDailyInitCompleted();


        } catch (Exception e) {
            log.error("초기화 작업 실행 중 에러", e);
        }
    }
}
