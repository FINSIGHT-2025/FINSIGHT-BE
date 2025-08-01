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
    public Job fundDailyInitJob() {
        return jobBuilderFactory.get("fundDailyInitJob")
                .start(fundDailyInitStep())
                .build();
    }

    @Bean
    public Step fundDailyInitStep() {
        return stepBuilderFactory.get("fundDailyInitStep")
                .tasklet((contribution, chunkContext) -> {
                    influxWriteService.deleteAllFromMeasurement("fund_daily");

                    for (int i = 10; i >= 1; i--) {
                        double fund_nav = 42000 + Math.random() * 20000000;
                        double fund_aum = 2000 + Math.random() * 200000000;

                        Instant timestamp = Instant.now().minusSeconds(60L * 60 * 24 * i);
                        influxWriteService.writeFundDaily(fund_nav, fund_aum, timestamp);
                    }

                    System.out.println("Fund Daily Init 저장 완료");
                    return RepeatStatus.FINISHED;
                }).build();
    }

    @Bean
    public Job etfDailyInitJob() {
        return jobBuilderFactory.get("etfDailyInitJob")
                .start(etfDailyInitStep())
                .build();
    }

    @Bean
    public Step etfDailyInitStep() {
        return stepBuilderFactory.get("etfDailyInitStep")
                .tasklet((contribution, chunkContext) -> {
                    influxWriteService.deleteAllFromMeasurement("etf_daily");

                    for (int i = 10; i >= 1; i--) {
                        double etf_nav = 42000 + Math.random() * 20000000;

                        Instant timestamp = Instant.now().minusSeconds(60L * 60 * 24 * i);
                        influxWriteService.writeEtfDaily(etf_nav, timestamp);
                    }

                    System.out.println("Etf Daily Init 저장 완료");
                    return RepeatStatus.FINISHED;
                }).build();
    }

    @Bean
    public Job etfRealtimeInitJob() {
        return jobBuilderFactory.get("etfRealtimeInitJob")
                .start(etfRealtimeInitStep())
                .build();
    }

    @Bean
    public Step etfRealtimeInitStep() {
        return stepBuilderFactory.get("etfRealtimeInitStep")
                .tasklet((contribution, chunkContext) -> {
                    influxWriteService.deleteAllFromMeasurement("etf_realtime");

                    for (int i = 10; i >= 1; i--) {
                        double etf_price = 2300 + Math.random() * 100000000;
                        double etf_volume = 100 + Math.random() * 50000000;

                        Instant timestamp = Instant.now().minusSeconds(60L * i); // 1분 간격 과거 데이터
                        influxWriteService.writeEtfRealtime(etf_price, etf_volume, timestamp);
                    }

                    System.out.println("ETF Realtime Init 저장 완료");
                    return RepeatStatus.FINISHED;
                }).build();
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
