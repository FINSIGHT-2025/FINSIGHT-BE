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


    // 1. Fund Init - 2개 step
    @Bean
    public Job fundInitJob() {
        return jobBuilderFactory.get("fundInitJob")
                .start(fundAumInitStep())
                .next(fundNavInitStep())
                .build();
    }

    @Bean
    public Step fundAumInitStep() {
        return stepBuilderFactory.get("fundAumInitStep")
                .tasklet((contribution, chunkContext) -> {
                    influxWriteService.deleteAllFromMeasurement("fund_aum");

                    String date = LocalDate.now().toString();
                    String uri = tradeDataUrl + "/fund/fund_aum/prev?date=" + date;

                    String raw = webClient.get()
                            .uri(uri)
                            .accept(MediaType.APPLICATION_JSON)
                            .retrieve()
                            .bodyToMono(String.class)
                            .doOnError(error -> log.error("❌ fund_aum API 실패", error))
                            .block();

                    ObjectMapper mapper = new ObjectMapper();
                    Map<String, Object> response = mapper.readValue(raw, new TypeReference<>() {});
                    List<Map<String, Object>> fundList = (List<Map<String, Object>>) response.get("data");

                    for (Map<String, Object> fund : fundList) {
                        String fundCode = (String) fund.get("fund_code");
                        double fundAum = ((Number) fund.get("aum")).doubleValue();
                        Instant timestamp = Instant.parse((String) fund.get("timestamp"));

                        influxWriteService.writeFundAum(fundCode, fundAum, timestamp);
                    }

                    log.info("✅ fund_aum 저장 완료: {}건", fundList.size());
                    return RepeatStatus.FINISHED;
                }).build();
    }

    @Bean
    public Step fundNavInitStep() {
        return stepBuilderFactory.get("fundNavInitStep")
                .tasklet((contribution, chunkContext) -> {
                    influxWriteService.deleteAllFromMeasurement("fund_nav");

                    String date = LocalDate.now().toString();
                    String uri = tradeDataUrl + "/fund/fund_nav/prev?date=" + date;

                    String raw = webClient.get()
                            .uri(uri)
                            .accept(MediaType.APPLICATION_JSON)
                            .retrieve()
                            .bodyToMono(String.class)
                            .doOnError(error -> log.error("❌ fund_nav API 실패", error))
                            .block();

                    ObjectMapper mapper = new ObjectMapper();
                    Map<String, Object> response = mapper.readValue(raw, new TypeReference<>() {});
                    List<Map<String, Object>> fundList = (List<Map<String, Object>>) response.get("data");

                    for (Map<String, Object> fund : fundList) {
                        String fundCode = (String) fund.get("fund_code");
                        double fundNav = ((Number) fund.get("nav")).doubleValue();
                        Instant timestamp = Instant.parse((String) fund.get("timestamp"));

                        influxWriteService.writeFundNav(fundCode, fundNav, timestamp);
                    }

                    log.info("✅ fund_nav 저장 완료: {}건", fundList.size());
                    return RepeatStatus.FINISHED;
                }).build();
    }


    // 2. Etf Init - 1개 step
    @Bean
    public Job etfInitJob() {
        return jobBuilderFactory.get("etfInitJob")
                .start(etfNavInitStep())
                .build();
    }

    @Bean
    public Step etfNavInitStep() {
        return stepBuilderFactory.get("etfNavInitStep")
                .tasklet((contribution, chunkContext) -> {
                    influxWriteService.deleteAllFromMeasurement("etf_nav");

                    String date = LocalDate.now().toString();
                    String uri = tradeDataUrl + "/etf/etf_nav/prev?date=" + date;

                    String raw = webClient.get()
                            .uri(uri)
                            .accept(MediaType.APPLICATION_JSON)
                            .retrieve()
                            .bodyToMono(String.class)
                            .doOnError(error -> log.error("❌ etf_nav (prev) API 실패", error))
                            .block();

                    ObjectMapper mapper = new ObjectMapper();
                    Map<String, Object> response = mapper.readValue(raw, new TypeReference<>() {});
                    List<Map<String, Object>> etfList = (List<Map<String, Object>>) response.get("data");

                    for (Map<String, Object> etf : etfList) {
                        String etfCode = (String) etf.get("product_code");
                        double etfNav = ((Number) etf.get("etf_nav")).doubleValue();
                        Instant timestamp = Instant.parse((String) etf.get("timestamp"));

                        influxWriteService.writeEtfNav(etfCode, etfNav, timestamp);
                    }

                    log.info("✅ 과거 etf_nav 저장 완료: {}건", etfList.size());
                    return RepeatStatus.FINISHED;
                }).build();
    }


    // 3. Fund Daily - 2개 step
    @Bean
    public Job fundDailyJob() {
        return jobBuilderFactory.get("fundDailyJob")
                .start(fundAumStep())
                .next(fundNavStep())
                .build();
    }

    @Bean
    public Step fundAumStep() {
        return stepBuilderFactory.get("fundAumStep")
                .tasklet((contribution, chunkContext) -> {
                    String date = LocalDate.now().toString();
                    String uri = tradeDataUrl + "/fund/fund_aum?date=" + date;

                    String raw = webClient.get()
                            .uri(uri)
                            .accept(MediaType.APPLICATION_JSON)
                            .retrieve()
                            .bodyToMono(String.class)
                            .doOnError(error -> log.error("❌ fund_aum API 실패", error))
                            .block();

                    ObjectMapper mapper = new ObjectMapper();
                    Map<String, Object> response = mapper.readValue(raw, new TypeReference<>() {});
                    List<Map<String, Object>> fundList = (List<Map<String, Object>>) response.get("data");

                    for (Map<String, Object> fund : fundList) {
                        String fundCode = (String) fund.get("fund_code");
                        double fundAum = ((Number) fund.get("aum")).doubleValue();
                        Instant timestamp = Instant.parse((String) fund.get("timestamp"));

                        influxWriteService.writeFundAum(fundCode, fundAum, timestamp);
                    }

                    log.info("✅ 오늘 fund_aum 저장 완료: {}건", fundList.size());
                    return RepeatStatus.FINISHED;
                }).build();
    }

    @Bean
    public Step fundNavStep() {
        return stepBuilderFactory.get("fundNavStep")
                .tasklet((contribution, chunkContext) -> {
                    String date = LocalDate.now().toString();
                    String uri = tradeDataUrl + "/fund/fund_nav?date=" + date;

                    String raw = webClient.get()
                            .uri(uri)
                            .accept(MediaType.APPLICATION_JSON)
                            .retrieve()
                            .bodyToMono(String.class)
                            .doOnError(error -> log.error("❌ fund_nav API 실패", error))
                            .block();

                    ObjectMapper mapper = new ObjectMapper();
                    Map<String, Object> response = mapper.readValue(raw, new TypeReference<>() {});
                    List<Map<String, Object>> fundList = (List<Map<String, Object>>) response.get("data");

                    for (Map<String, Object> fund : fundList) {
                        String fundCode = (String) fund.get("fund_code");
                        double fundNav = ((Number) fund.get("nav")).doubleValue();
                        Instant timestamp = Instant.parse((String) fund.get("timestamp"));

                        influxWriteService.writeFundNav(fundCode, fundNav, timestamp);
                    }

                    log.info("✅ 오늘 fund_nav 저장 완료: {}건", fundList.size());
                    return RepeatStatus.FINISHED;
                }).build();
    }


    // 4. Etf Daily - 1개 step
    @Bean
    public Job etfDailyJob() {
        return jobBuilderFactory.get("etfDailyJob")
                .start(etfNavStep())
                .build();
    }

    @Bean
    public Step etfNavStep() {
        return stepBuilderFactory.get("etfNavStep")
                .tasklet((contribution, chunkContext) -> {
                    String date = LocalDate.now().toString();
                    String uri = tradeDataUrl + "/etf/etf_nav?date=" + date;

                    String raw = webClient.get()
                            .uri(uri)
                            .accept(MediaType.APPLICATION_JSON)
                            .retrieve()
                            .bodyToMono(String.class)
                            .doOnError(error -> log.error("❌ etf_nav API 실패", error))
                            .block();

                    ObjectMapper mapper = new ObjectMapper();
                    Map<String, Object> response = mapper.readValue(raw, new TypeReference<>() {});
                    List<Map<String, Object>> etfList = (List<Map<String, Object>>) response.get("data");

                    for (Map<String, Object> etf : etfList) {
                        String etfCode = (String) etf.get("product_code");
                        double etfNav = ((Number) etf.get("etf_nav")).doubleValue();
                        Instant timestamp = Instant.parse((String) etf.get("timestamp"));

                        influxWriteService.writeEtfNav(etfCode, etfNav, timestamp);
                    }

                    log.info("✅ 오늘 etf_nav 저장 완료: {}건", etfList.size());
                    return RepeatStatus.FINISHED;
                }).build();
    }
}
