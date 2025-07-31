package com.finsight.backend.config;

import com.finsight.backend.service.InfluxWriteService;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Instant;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private InfluxWriteService influxWriteService;

    public BatchConfig(JobBuilderFactory jobBuilderFactory,
                       StepBuilderFactory stepBuilderFactory,
                       InfluxWriteService influxWriteService) {

        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.influxWriteService = influxWriteService;
    }

    @Bean
    public Job fundDailyJob() {
        return jobBuilderFactory.get("fundDailyJob")
                .start(fundDailyStep())
                .build();
    }

    @Bean
    public Step fundDailyStep() {
        return stepBuilderFactory.get("fundDailyStep")
                .tasklet((contribution, chunkContext) -> {
                    double fund_nav = 42000 + Math.random() * 200;
                    double fund_aum = 2000 + Math.random() * 2000;
                    influxWriteService.writeFundDaily(fund_nav, fund_aum, Instant.now());

                    System.out.println("Fund Daily 저장 완료");
                    return RepeatStatus.FINISHED;
                }).build();
    }

    @Bean
    public Job etfDailyJob() {
        return jobBuilderFactory.get("etfDailyJob")
                .start(etfDailyStep())
                .build();
    }

    @Bean
    public Step etfDailyStep() {
        return stepBuilderFactory.get("etfDailyStep")
                .tasklet((contribution, chunkContext) -> {
                    double etf_nav = 2300 + Math.random() * 1000;
                    influxWriteService.writeEtfDaily(etf_nav, Instant.now());

                    System.out.println("ETF Daily 저장 완료");
                    return RepeatStatus.FINISHED;
                }).build();
    }
}
