package com.finsight.backend.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finsight.backend.dto.external.ExchangeApiResponseDTO;
import com.finsight.backend.dto.response.ExchangeRateDTO;
import com.finsight.backend.dto.response.ExchangeResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.text.DecimalFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ExchangeRateService {

    @Value("${exchange.api.key}")
    private String apiKey;


    private final ObjectMapper objectMapper = new ObjectMapper();
    private RestTemplate exchangeRestTemplate;

    @PostConstruct
    public void init() {
        this.exchangeRestTemplate = createExchangeRestTemplate();
        initializeApiSession();
    }

    private RestTemplate createExchangeRestTemplate() {
        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultCookieStore(new BasicCookieStore())
                .build();
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);
        factory.setConnectTimeout(5000);
        factory.setReadTimeout(5000);

        RestTemplate restTemplate = new RestTemplate(factory);
        restTemplate.setInterceptors(Collections.singletonList((request, body, execution) -> {
            HttpHeaders headers = request.getHeaders();
            headers.add(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            return execution.execute(request, body);
        }));

        return restTemplate;
    }

    private void initializeApiSession() {
        // API 키가 주입된 후에 호출되어야 하므로 init() 메소드 내부로 이동
        URI initUri = UriComponentsBuilder.fromHttpUrl(EXTERNAL_API_URL)
                .queryParam("authkey", apiKey)
                .queryParam("searchdate", LocalDate.now().format(API_DATE_FORMAT))
                .queryParam("data", "AP01")
                .build(true).toUri();
        try {
            log.info("API 세션 초기화를 위한 핸드셰이크 요청을 보냅니다.");
            HttpEntity<String> entity = new HttpEntity<>(new HttpHeaders()); // 헤더 인터셉터가 적용되도록 빈 HttpEntity 사용
            exchangeRestTemplate.exchange(initUri, HttpMethod.GET, entity, String.class);
            log.info("핸드셰이크 요청 성공. (기존 세션 유효)");
        } catch (Exception e) {
            log.info("핸드셰이크 완료. 세션 쿠키가 발급되었습니다. (예외는 정상 동작)");
        }
    }

    public ExchangeResponseDTO getProcessedExchangeRates() throws IOException {
        LocalDate baseDate = getBaseDate();
        LocalDate previousDate = getPreviousDate(baseDate);

        log.info("환율 디버깅 : 계산된 기준일: {}, 이전 영업일: {}", baseDate, previousDate);

        Map<String, ExchangeApiResponseDTO> todayRatesMap = fetchRatesToMap(baseDate, false);
        Map<String, ExchangeApiResponseDTO> yestRatesMap = fetchRatesToMap(previousDate, false);

        log.info("오늘 환율 데이터 {}개, 이전 영업일 환율 데이터 {}개 수신 완료", todayRatesMap.size(), yestRatesMap.size());

        // ... (이하 로직은 동일)
        List<ExchangeRateDTO> result = todayRatesMap.values().stream()
                .map(todayItem -> {
                    ExchangeApiResponseDTO yestItem = yestRatesMap.get(todayItem.getCurUnit());

                    ExchangeRateDTO.ExchangeRateDTOBuilder builder = ExchangeRateDTO.builder()
                            .cur_unit(todayItem.getCurUnit())
                            .cur_nm(todayItem.getCurNm())
                            .deal_bas_r(todayItem.getDealBasR());

                    if (yestItem != null) {
                        try {
                            BigDecimal todayRate = new BigDecimal(todayItem.getDealBasR().replace(",", ""));
                            BigDecimal yestRate = new BigDecimal(yestItem.getDealBasR().replace(",", ""));

                            if (yestRate.compareTo(BigDecimal.ZERO) != 0) {
                                BigDecimal diff = todayRate.subtract(yestRate);
                                BigDecimal percentage = diff.divide(yestRate, 4, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100));

                                DecimalFormat df = new DecimalFormat("0.00");
                                builder.diff(df.format(diff));
                                builder.percentage(df.format(percentage));
                            }
                        } catch (NumberFormatException e) {
                            log.error("[{}] 환율 값 숫자 변환 오류. 오늘: '{}', 어제: '{}'",
                                    todayItem.getCurUnit(), todayItem.getDealBasR(), yestItem.getDealBasR(), e);
                        }
                    }
                    return builder.build();
                })
                .collect(Collectors.toList());

        return new ExchangeResponseDTO(baseDate.format(DISPLAY_DATE_FORMAT), result);
    }

    private Map<String, ExchangeApiResponseDTO> fetchRatesToMap(LocalDate date, boolean isRetry) throws IOException {
        String dateStr = date.format(API_DATE_FORMAT);
        URI uri = UriComponentsBuilder.fromHttpUrl(EXTERNAL_API_URL)
                .queryParam("authkey", apiKey)
                .queryParam("searchdate", dateStr)
                .queryParam("data", "AP01")
                .build(true).toUri();

        try {
            // exchange 메소드 호출 시에도 인터셉터가 적용되므로 빈 HttpEntity를 넘겨줍니다.
            HttpEntity<String> entity = new HttpEntity<>(new HttpHeaders());
            ResponseEntity<String> response = exchangeRestTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
            String jsonString = response.getBody();

            if (jsonString == null || jsonString.isEmpty() || jsonString.equals("[]")) {
                return Collections.emptyMap();
            }

            List<ExchangeApiResponseDTO> rates = objectMapper.readValue(jsonString, new TypeReference<List<ExchangeApiResponseDTO>>() {});

            return rates.stream()
                    .filter(rate -> TARGET_CURRENCIES.contains(rate.getCurUnit()))
                    .collect(Collectors.toMap(ExchangeApiResponseDTO::getCurUnit, Function.identity(), (r1, r2) -> r1));
        } catch (Exception e) {
            // <<< [수정] 에러 발생 시, 재시도 안한 경우에만 핸드셰이크 후 재시도
            if (!isRetry && e instanceof ResourceAccessException) {
                log.warn("{} 날짜 환율 조회 실패. API 핸드셰이크 후 재시도합니다. 오류: {}", date, e.getMessage());
                initializeApiSession(); // 핸드셰이크 실행
                return fetchRatesToMap(date, true); // 재시도 플래그를 true로 하여 딱 한번만 더 시도
            }

            // 재시도임에도 실패했거나, 다른 종류의 에러일 경우 최종 실패 처리
            log.error("{} 날짜의 환율 정보 조회 중 최종 오류 발생. URI: {}", date, uri, e);
            return Collections.emptyMap();
        }
    }

    // ... (날짜 계산 헬퍼 메소드들은 그대로)
    private static final String EXTERNAL_API_URL = "https://www.koreaexim.go.kr/site/program/financial/exchangeJSON";
    private static final Set<String> TARGET_CURRENCIES = Set.of("CNH", "EUR", "JPY(100)", "USD", "THB");
    private static final Set<LocalDate> HOLIDAYS = Set.of(
            LocalDate.of(2025, 1, 1), LocalDate.of(2025, 3, 1),
            LocalDate.of(2025, 5, 5), LocalDate.of(2025, 6, 6),
            LocalDate.of(2025, 8, 15), LocalDate.of(2025, 9, 12),
            LocalDate.of(2025, 9, 13), LocalDate.of(2025, 9, 14),
            LocalDate.of(2025, 10, 3), LocalDate.of(2025, 10, 9),
            LocalDate.of(2025, 12, 25)
    );
    private static final DateTimeFormatter API_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final DateTimeFormatter DISPLAY_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy.MM.dd");
    private boolean isNonWorkingDay(LocalDate date) {
        DayOfWeek day = date.getDayOfWeek();
        return day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY || HOLIDAYS.contains(date);
    }

    private LocalDate getPreviousWorkingDay(LocalDate date) {
        LocalDate newDate = date;
        while (isNonWorkingDay(newDate)) {
            newDate = newDate.minusDays(1);
        }
        return newDate;
    }

    private LocalDate getBaseDate() {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        LocalDateTime cutoff = now.toLocalDate().atTime(11, 0);

        LocalDate base = now.isBefore(cutoff) ? now.toLocalDate().minusDays(1) : now.toLocalDate();
        return getPreviousWorkingDay(base);
    }

    private LocalDate getPreviousDate(LocalDate baseDate) {
        return getPreviousWorkingDay(baseDate.minusDays(1));
    }
}