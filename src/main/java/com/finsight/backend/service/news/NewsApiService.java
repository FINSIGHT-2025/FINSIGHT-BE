package com.finsight.backend.service.news;

import com.finsight.backend.dto.external.NewsApiRequestDTO;
import com.finsight.backend.dto.external.NewsApiResponseDTO;
import com.finsight.backend.repository.mapper.NewsMapper;

import com.finsight.backend.service.SentimentAnalysisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import reactor.core.publisher.Mono;

import org.openkoreantext.processor.OpenKoreanTextProcessorJava;
import org.openkoreantext.processor.tokenizer.KoreanTokenizer;
import org.openkoreantext.processor.util.KoreanPos;

import scala.collection.Seq;
import scala.collection.JavaConverters;

import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;
import org.apache.commons.text.similarity.LevenshteinDistance;

@Slf4j
@Service
@RequiredArgsConstructor
public class NewsApiService {

    private final WebClient webClient;
    private final SentimentAnalysisService sentimentAnalysisService;
    private final NewsMapper newsMapper;

    @Value("${news.url}")
    private String url;

    @Value("${news.api_key}")
    private String api_key;

    // Levenshtein
    private final LevenshteinDistance levenshtein = new LevenshteinDistance();
    private final int LEVENSHTEIN_THRESHOLD = 3;

    private static final Set<String> STOP_WORDS = Set.of(
            "시장", "산업", "업계", "기업", "금융", "투자", "상품", "자산", "정보", "서비스",
            "분야", "내용", "관련", "기술", "활용", "위해", "대해", "통해", "제공", "최근",
            "전망", "상황", "부분", "중심", "요인", "가운데", "때문", "수준", "기반", "가능성",
            "변화", "결과", "기준", "대상", "의미", "현재", "의견", "영향", "성과", "요소",
            "대응", "강조", "방식", "기간", "경우", "현상", "현실", "방안", "차원", "동향"
    );

    private static final Set<String> SPLIT_HINT_WORDS = Set.of(
            "ai", "esg", "etf", "펀드", "로봇", "챗봇", "딥러닝", "머신러닝", "빅데이터",
            "가치", "성장", "리스크", "배당", "지수", "전략", "테마", "모델", "알고리즘",
            "자동화", "플랫폼", "클라우드", "블록체인", "인덱스", "리밸런싱", "인공지능", "반도체"
    );


    public Mono<NewsApiResponseDTO> fetchNews(NewsApiRequestDTO requestDTO) {
        // URI 생성 로직
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("date_from", requestDTO.getDateFrom().toString())
                .queryParam("date_to", requestDTO.getDateTo().toString())
                .queryParam("order", requestDTO.getOrder())
                .queryParam("page_size", requestDTO.getPageSize());

        String symbols = requestDTO.getSymbols();
        if (symbols != null && !symbols.isBlank()) {
            uriBuilder.queryParam("symbols", symbols);
        }

        if (!CollectionUtils.isEmpty(requestDTO.getKeywords())) {
            String keywords = requestDTO.getKeywords().stream()
                    .map(keyword -> "\"" + keyword + "\"")
                    .collect(Collectors.joining(" OR "));
            uriBuilder.queryParam("keyword", keywords);
        }

        URI uri = uriBuilder.build().encode().toUri();
        log.info("Requesting URI = {}", uri.toASCIIString());

        return callExternalApi(uri);
    }



    private Mono<NewsApiResponseDTO> callExternalApi(URI uri){
            log.info("Calling external news API : {}", uri);
            return webClient.get()
                    .uri(uri)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + api_key)
                    .retrieve()
                    .bodyToMono(NewsApiResponseDTO.class)
                    .switchIfEmpty(Mono.error(new RuntimeException("API 응답이 비어있습니다.")))
                    .map(this::processResponse);

    }

    private NewsApiResponseDTO processResponse(NewsApiResponseDTO responseDTO) {
        if(responseDTO.getData() == null || responseDTO.getData().isEmpty()) {
            log.warn("News API 데이터가 비어있습니다.");
            return responseDTO;
        }
        List<NewsApiResponseDTO.NewsData> processedData = responseDTO.getData().stream()
                // ✨ 감성 분석 전에 DB 존재 여부 확인
                .filter(data -> !newsMapper.existsNewsId(data.getId()) && !newsMapper.existsNewsTitle(data.getTitle()))
                .map(data -> {
                    float sentimentScore = sentimentAnalysisService.getWeightedSentimentScore(data.getTitle());
                    return data.toBuilder().sentimentScore(sentimentScore).build();
                })
                .map(this::addKeywordsToData)
                .collect(Collectors.toList());

        return responseDTO.toBuilder()
                .data(processedData)
                .build();
    }


    private NewsApiResponseDTO.NewsData addKeywordsToData(NewsApiResponseDTO.NewsData data) {
        // 정규화
        CharSequence normalized = OpenKoreanTextProcessorJava.normalize(data.getSummary());

        // 토크나이즈
        Seq<KoreanTokenizer.KoreanToken> tokens = OpenKoreanTextProcessorJava.tokenize(normalized);
        List<KoreanTokenizer.KoreanToken> tokenList = JavaConverters.seqAsJavaList(tokens);

        // entities와 companies로부터 고유명사 후보 확보
        Set<String> namedEntities = new HashSet<>();
        if (data.getCompanies() != null) {
            data.getCompanies().stream()
                    .map(NewsApiResponseDTO.Company::getName)
                    .map(String::toLowerCase)
                    .forEach(namedEntities::add);
        }
        if (data.getEntities() != null) {
            data.getEntities().stream()
                    .map(NewsApiResponseDTO.Entity::getName)
                    .map(String::toLowerCase)
                    .forEach(namedEntities::add);
        }

        // 연속된 명사/알파벳 토큰 병합
        List<String> mergedTokens = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < tokenList.size(); i++) {
            KoreanTokenizer.KoreanToken token = tokenList.get(i);
            var pos = token.pos();
            String word = token.text();

            if (pos.equals(KoreanPos.Noun()) || pos.equals(KoreanPos.Alpha())) {
                if (sb.length() == 0) {
                    sb.append(word);
                } else {
                    KoreanTokenizer.KoreanToken prevToken = tokenList.get(i - 1);
                    int prevEnd = prevToken.offset() + prevToken.text().length();
                    int currStart = token.offset();
                    if (currStart <= prevEnd + 1) {
                        sb.append(word);
                    } else {
                        mergedTokens.add(sb.toString().toLowerCase());
                        sb.setLength(0);
                        sb.append(word);
                    }
                }
            } else {
                if (sb.length() > 0) {
                    mergedTokens.add(sb.toString().toLowerCase());
                    sb.setLength(0);
                }
            }
        }
        if (sb.length() > 0) {
            mergedTokens.add(sb.toString().toLowerCase());
        }

        // TF 계산, 교정, 복합명사 분해
        Map<String, Integer> tfMap = new HashMap<>();
        for (String tokenText : mergedTokens) {
            if (tokenText.length() >= 2 && !STOP_WORDS.contains(tokenText)) {
                String correctedToken = correctTokenByEntities(tokenText, namedEntities);
                tfMap.put(correctedToken, tfMap.getOrDefault(correctedToken, 0) + 1);

                for (String hint : SPLIT_HINT_WORDS) {
                    if (correctedToken.contains(hint) && !correctedToken.equals(hint)) {
                        String[] splitParts = correctedToken.split("(?i)" + hint);
                        if (splitParts.length >= 2) {
                            tfMap.put(hint, tfMap.getOrDefault(hint, 0) + 1);
                            for (String part : splitParts) {
                                if (part.length() >= 2 && !STOP_WORDS.contains(part)) {
                                    tfMap.put(part, tfMap.getOrDefault(part, 0) + 1);
                                }
                            }
                        }
                    }
                }
            }
        }

        // 제목 포함 여부에 따른 가중치 적용 및 Top 2 키워드 선정
        Set<String> titleWords = Arrays.stream(data.getTitle().toLowerCase().split("\\s+"))
                .collect(Collectors.toSet());

        List<String> topKeywords = tfMap.entrySet().stream()
                .sorted((a, b) -> {
                    int aWeight = titleWords.contains(a.getKey()) ? 2 : 1;
                    int bWeight = titleWords.contains(b.getKey()) ? 2 : 1;
                    return Integer.compare(b.getValue() * bWeight, a.getValue() * aWeight);
                })
                .limit(2)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        // 기존 NewsData에 키워드를 추가하여 새로운 NewsData 객체 반환
        return data.toBuilder()
                .keywords(topKeywords)
                .build();
    }

    /**
     * 토큰을 엔티티명 집합과 비교하여
     * 1) 엔티티명이 토큰을 포함하거나 토큰이 엔티티명에 포함된 경우 우선 교정
     * 2) Levenshtein 거리 임계값 이하인 경우 교정
     * 3) 해당 조건에 맞는 엔티티가 없으면 원 토큰 그대로 반환
     */
    private String correctTokenByEntities(String token, Set<String> namedEntities) {
        String lowerCaseToken = token.toLowerCase();
        // 포함 관계 우선 검사
        for (String entity : namedEntities) {
            if (entity.contains(lowerCaseToken) || lowerCaseToken.contains(entity)) {
                return entity;
            }
        }

        // Levenshtein 거리 검사
        for (String entity : namedEntities) {
            if (levenshtein.apply(lowerCaseToken, entity) <= LEVENSHTEIN_THRESHOLD) {
                return entity;
            }
        }

        // 교정 대상 없으면 원본 반환
        return lowerCaseToken;
    }
}
