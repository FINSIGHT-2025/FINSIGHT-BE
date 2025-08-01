package com.finsight.backend.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finsight.backend.service.InfluxWriteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;


import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Slf4j
@Configuration
@EnableBatchProcessing
public class BatchConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private InfluxWriteService influxWriteService;
    private WebClient webClient;

    public BatchConfig(JobBuilderFactory jobBuilderFactory,
                       StepBuilderFactory stepBuilderFactory,
                       InfluxWriteService influxWriteService,
                       WebClient webClient) {

        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.influxWriteService = influxWriteService;
        this.webClient = webClient;
    }

    @Value("${tradedata.url}")
    private String tradeDataUrl;

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

                    // 어제 날짜 기준
                    String date = LocalDate.now().minusDays(1).toString();
                    String uri = tradeDataUrl + "/fund/fund_aum/prev?date=" + date;

                    String raw = webClient.get()
                            .uri(uri)
                            .accept(MediaType.APPLICATION_JSON)
                            .retrieve()
                            .bodyToMono(String.class)
                            .doOnError(error -> log.error("❌ API call failed", error))
                            .block();

                    // JSON 파싱
                    ObjectMapper mapper = new ObjectMapper();
                    Map<String, Object> response = mapper.readValue(raw, new TypeReference<>() {});
                    List<Map<String, Object>> fundList = (List<Map<String, Object>>) response.get("data");

                    for (Map<String, Object> fund : fundList) {
                        String fundCode = (String) fund.get("fund_code");
                        String fundName = (String) fund.get("fund_name");
                        double fundAum = ((Number) fund.get("aum")).doubleValue();
                        Instant timestamp = Instant.parse((String) fund.get("timestamp"));

                        log.info("✅ 과거 저장할 펀드: {} - {} - {} - {}", fundCode, fundName, fundAum, timestamp);
                        influxWriteService.writeFundDaily(fundCode, fundName, fundAum, timestamp);
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
//                        influxWriteService.writeEtfRealtime(etf_price, etf_volume, timestamp);
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
                    // 오늘 날짜 기준
                    String date = java.time.LocalDate.now().toString();
                    String uri = tradeDataUrl + "/fund/fund_aum?date=" + date;

                    String raw = webClient.get()
                            .uri(uri)
                            .accept(MediaType.APPLICATION_JSON)
                            .retrieve()
                            .bodyToMono(String.class)
                            .doOnError(error -> log.error("❌ API call failed", error))
                            .block();

                    // JSON 파싱
                    ObjectMapper mapper = new ObjectMapper();
                    Map<String, Object> response = mapper.readValue(raw, new TypeReference<>() {});
                    List<Map<String, Object>> fundList = (List<Map<String, Object>>) response.get("data");

                    for (Map<String, Object> fund : fundList) {
                        String fundCode = (String) fund.get("fund_code");
                        String fundName = (String) fund.get("fund_name");
                        double fundAum = ((Number) fund.get("aum")).doubleValue();
                        Instant timestamp = Instant.parse((String) fund.get("timestamp"));

                        log.info("✅ 오늘 저장할 펀드: {} - {} - {} - {}", fundCode, fundName, fundAum, timestamp);
                        influxWriteService.writeFundDaily(fundCode, fundName, fundAum, timestamp);
                    }

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
