package com.finsight.backend.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finsight.backend.dto.external.ExchangeApiResponseDTO;
import com.finsight.backend.dto.response.ExchangeRateDTO;
import com.finsight.backend.dto.response.ExchangeResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

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
import org.springframework.beans.factory.annotation.Value;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExchangeRateService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${exchange.api.key}")
    private String apiKey;

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

    public ExchangeResponseDTO getProcessedExchangeRates() throws IOException {
        LocalDate baseDate = getBaseDate();
        LocalDate previousDate = getPreviousDate(baseDate);

        log.info("환율 디버깅 : 계산된 기준일: {}, 이전 영업일: {}", baseDate, previousDate);

        // 외부 API 데이터를 ExchangeApiResponseDTO로 받음
        Map<String, ExchangeApiResponseDTO> todayRatesMap = fetchRatesToMap(baseDate);
        Map<String, ExchangeApiResponseDTO> yestRatesMap = fetchRatesToMap(previousDate);

        log.info("오늘 환율 데이터 {}개, 이전 영업일 환율 데이터 {}개 수신 완료", todayRatesMap.size(), yestRatesMap.size());

        // 최종 클라이언트 응답 데이터인 ExchangeRateDTO 목록 생성
        List<ExchangeRateDTO> result = todayRatesMap.values().stream()
                .map(todayItem -> {
                    ExchangeApiResponseDTO yestItem = yestRatesMap.get(todayItem.getCurUnit());

                    ExchangeRateDTO.ExchangeRateDTOBuilder builder = ExchangeRateDTO.builder()
                            .cur_unit(todayItem.getCurUnit())
                            .cur_nm(todayItem.getCurNm())
                            .deal_bas_r(todayItem.getDealBasR());

                    if (yestItem != null) {
                        BigDecimal todayRate = new BigDecimal(todayItem.getDealBasR().replace(",", ""));
                        BigDecimal yestRate = new BigDecimal(yestItem.getDealBasR().replace(",", ""));

                        if (yestRate.compareTo(BigDecimal.ZERO) != 0) {
                            BigDecimal diff = todayRate.subtract(yestRate);
                            BigDecimal percentage = diff.divide(yestRate, 4, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100));

                            DecimalFormat df = new DecimalFormat("0.00");
                            builder.diff(df.format(diff));
                            builder.percentage(df.format(percentage));
                        }
                    }
                    return builder.build();
                })
                .collect(Collectors.toList());

        // 최종 포장 객체인 ExchangeResponseDTO로 감싸서 반환
        return new ExchangeResponseDTO(baseDate.format(DISPLAY_DATE_FORMAT), result);
    }

    private Map<String, ExchangeApiResponseDTO> fetchRatesToMap(LocalDate date) throws IOException {
        String dateStr = date.format(API_DATE_FORMAT);
        URI uri = UriComponentsBuilder.fromHttpUrl(EXTERNAL_API_URL)
                .queryParam("authkey", apiKey)
                .queryParam("searchdate", dateStr)
                .queryParam("data", "AP01")
                .build(true).toUri();
        log.info("환율 외부 API 호출: {}", uri);

        String jsonString = restTemplate.getForObject(uri, String.class);
        if (jsonString == null || jsonString.isEmpty() || jsonString.equals("[]")) {
            return Collections.emptyMap();
        }
        log.info("환율 외부 API 응답 ({}): {}", dateStr, jsonString);
        // 외부 API 응답을 ExchangeApiResponseDTO 리스트로 변환
        List<ExchangeApiResponseDTO> rates = objectMapper.readValue(jsonString, new TypeReference<List<ExchangeApiResponseDTO>>() {});

        return rates.stream()
                .filter(rate -> TARGET_CURRENCIES.contains(rate.getCurUnit()))
                .collect(Collectors.toMap(ExchangeApiResponseDTO::getCurUnit, Function.identity(), (r1, r2) -> r1));
    }

    // --- 날짜 계산 헬퍼 메소드 (기존과 동일) ---
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
