package com.finsight.backend.service;

import com.finsight.backend.dto.external.NewsRequestDTO;
import com.finsight.backend.dto.external.NewsResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
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
public class NewsService {

    private final WebClient webClient;

    @Value("${news.url}")
    private String url;

    @Value("${news.api_key}")
    private String api_key;

    // Levenshtein 거리 계산기
    private final LevenshteinDistance levenshtein = new LevenshteinDistance();

    // 허용할 최대 편집 거리 임계값
    private final int LEVENSHTEIN_THRESHOLD = 3;

    public Mono<NewsResponseDTO> fetchNews(NewsRequestDTO requestDTO) {
        String symbolsParam = String.join(",", requestDTO.getSymbols());

        URI uri = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("symbols", symbolsParam)
                .queryParam("date_from", requestDTO.getDateFrom().toString())
                .queryParam("date_to", requestDTO.getDateTo().toString())
                .queryParam("order", requestDTO.getOrder())
                .queryParam("page_size", requestDTO.getPageSize())
                .build()
                .toUri();

        log.info("News : Calling external API : {}", uri);

        return webClient.get()
                .uri(uri)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + api_key)
                .retrieve()
                .bodyToMono(NewsResponseDTO.class)
                .switchIfEmpty(Mono.error(new RuntimeException("News : API 응답이 비어있습니다 (200 OK with empty body)")))
                .map(response -> {
                    List<NewsResponseDTO.NewsData> origin = response.getData();
                    if (origin == null) {
                        return response;
                    }

                    Set<String> stopWords = Set.of(
                            "시장", "산업", "업계", "기업", "금융", "투자", "상품", "자산", "정보", "서비스",
                            "분야", "내용", "관련", "기술", "활용", "위해", "대해", "통해", "제공", "최근",
                            "전망", "상황", "부분", "중심", "요인", "가운데", "때문", "수준", "기반", "가능성",
                            "변화", "결과", "기준", "대상", "의미", "현재", "의견", "영향", "성과", "요소",
                            "대응", "강조", "방식", "기간", "경우", "현상", "현실", "방안", "차원", "동향"
                    );

                    Set<String> splitHintWords = Set.of(
                            "ai", "esg", "etf", "펀드", "로봇", "챗봇", "딥러닝", "머신러닝", "빅데이터",
                            "가치", "성장", "리스크", "배당", "지수", "전략", "테마", "모델", "알고리즘",
                            "자동화", "플랫폼", "클라우드", "블록체인", "인덱스", "리밸런싱", "인공지능", "반도체"
                    );

                    // esg != null 인 기사만 필터링
                    List<NewsResponseDTO.NewsData> filteredOrigin = origin.stream()
                            .filter(data -> data.getEsg() != null)
                            .collect(Collectors.toList());

                    List<NewsResponseDTO.NewsData> processedData = new ArrayList<>();

                    for (NewsResponseDTO.NewsData data : filteredOrigin) {
                        // 정규화
                        CharSequence normalized = OpenKoreanTextProcessorJava.normalize(data.getSummary());

                        // 토크나이즈
                        Seq<KoreanTokenizer.KoreanToken> tokens = OpenKoreanTextProcessorJava.tokenize(normalized);
                        List<KoreanTokenizer.KoreanToken> tokenList = JavaConverters.seqAsJavaList(tokens);

                        // entities와 companies로부터 고유명사 후보 확보
                        Set<String> namedEntities = new HashSet<>();
                        if (data.getCompanies() != null) {
                            namedEntities.addAll(data.getCompanies().stream()
                                    .map(NewsResponseDTO.Company::getName)
                                    .map(String::toLowerCase)
                                    .collect(Collectors.toSet()));
                        }
                        if (data.getEntities() != null) {
                            namedEntities.addAll(data.getEntities().stream()
                                    .map(NewsResponseDTO.Entity::getName)
                                    .map(String::toLowerCase)
                                    .collect(Collectors.toSet()));
                        }

                        // 중복 제거
                        namedEntities = namedEntities.stream()
                                .map(String::toLowerCase)
                                .collect(Collectors.toSet());

                        // 1) 연속된 명사/알파벳 토큰 병합
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

                        // 2) TF 계산 및 교정
                        Map<String, Integer> tfMap = new HashMap<>();
                        for (String tokenText : mergedTokens) {
                            if (tokenText.length() >= 2 && !stopWords.contains(tokenText)) {

                                // 토큰과 가장 유사한 엔티티명으로 교정 시도
                                String correctedToken = correctTokenByEntities(tokenText, namedEntities);

                                tfMap.put(correctedToken, tfMap.getOrDefault(correctedToken, 0) + 1);

                                // 복합명사 분해 기준: splitHintWords 또는 namedEntities 포함 여부
                                for (String hint : splitHintWords) {
                                    if (correctedToken.contains(hint) && !correctedToken.equals(hint)) {
                                        String[] splitParts = correctedToken.split("(?i)" + hint);
                                        if (splitParts.length >= 2) {
                                            tfMap.put(hint, tfMap.getOrDefault(hint, 0) + 1);
                                            for (String part : splitParts) {
                                                if (part.length() >= 2 && !stopWords.contains(part)) {
                                                    tfMap.put(part, tfMap.getOrDefault(part, 0) + 1);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        // 제목 포함 여부에 따른 가중치 적용
                        Set<String> titleWords = Arrays.stream(data.getTitle().split("\\s+"))
                                .map(String::toLowerCase)
                                .collect(Collectors.toSet());

                        List<String> topKeywords = tfMap.entrySet().stream()
                                .sorted((a, b) -> {
                                    int aWeight = titleWords.contains(a.getKey()) ? 2 : 1;
                                    int bWeight = titleWords.contains(b.getKey()) ? 2 : 1;
                                    return Integer.compare(b.getValue() * bWeight, a.getValue() * aWeight);
                                })
                                .limit(3)
                                .map(Map.Entry::getKey)
                                .collect(Collectors.toList());

                        processedData.add(NewsResponseDTO.NewsData.builder()
                                .id(data.getId())
                                .sections(data.getSections())
                                .title(data.getTitle())
                                .publisher(data.getPublisher())
                                .author(data.getAuthor())
                                .summary(data.getSummary())
                                .highlight(data.getHighlight())
                                .score(data.getScore())
                                .imageUrl(data.getImageUrl())
                                .thumbnailUrl(data.getThumbnailUrl())
                                .contentUrl(data.getContentUrl())
                                .esg(data.getEsg())
                                .companies(data.getCompanies())
                                .entities(data.getEntities())
                                .publishedAt(data.getPublishedAt())
                                .keywords(topKeywords)
                                .build());
                    }

                    return NewsResponseDTO.builder()
                            .detail(response.getDetail())
                            .totalItems(response.getTotalItems())
                            .totalPages(response.getTotalPages())
                            .page(response.getPage())
                            .pageSize(response.getPageSize())
                            .data(processedData)
                            .build();
                })
                .doOnNext(response -> log.info("News Response message: {}", response.getDetail().getMessage()))
                .doOnError(error -> log.error("News : API call failed", error));
    }

    /**
     * 토큰을 엔티티명 집합과 비교하여
     * 1) 엔티티명이 토큰을 포함하거나 토큰이 엔티티명에 포함된 경우 우선 교정
     * 2) Levenshtein 거리 임계값 이하인 경우 교정
     * 3) 해당 조건에 맞는 엔티티가 없으면 원 토큰 그대로 반환
     */
    private String correctTokenByEntities(String token, Set<String> namedEntities) {
        token = token.toLowerCase();

        // 1) 포함 관계 우선 검사
        for (String entity : namedEntities) {
            if (entity.contains(token) || token.contains(entity)) {
                return entity;
            }
        }

        // 2) Levenshtein 거리 검사
        for (String entity : namedEntities) {
            int dist = levenshtein.apply(token, entity);
            if (dist <= LEVENSHTEIN_THRESHOLD) {
                return entity;
            }
        }

        // 3) 교정 대상 없으면 원본 반환
        return token;
    }
}
