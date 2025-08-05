package com.finsight.backend.service;


import com.google.cloud.language.v2.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class SentimentAnalysisService {

    private final LanguageServiceClient languageServiceClient;

    public float getSentimentScore(String text) { // 메서드명 및 반환 타입 변경
        try {
            String cleanText = preprocessText(text);
            Document doc = Document.newBuilder()
                    .setContent(cleanText)
                    .setType(Document.Type.PLAIN_TEXT)
                    .build();

            Sentiment sentiment = languageServiceClient.analyzeSentiment(doc).getDocumentSentiment();
            float score = sentiment.getScore();
            log.info("감성 분석 결과: '{}' (점수: {})", text, score);
            return score;

        } catch (Exception e) {
            log.error("Google Cloud 감성 분석 API 호출 중 오류가 발생했습니다.", e);
            return 0.0f;
        }
    }

    /**
     * 입력 텍스트 전처리 (따옴표, 이모지, 공백 등)
     */
    private String preprocessText(String text) {
        if (text == null) return "";
        return text
                .replaceAll("[“”‘’]", "\"")     // 따옴표 정규화
                .replaceAll("…", "...")         // 줄임표 정규화
                .replaceAll("[^\\p{L}\\p{N}\\p{P}\\p{Z}]", "") // 이모지 및 특수기호 제거
                .replaceAll("\\s+", " ")        // 중복 공백 제거
                .trim();
    }

    /**
     * 문장별 (score × magnitude) 가중 평균 감성 점수 계산
     */
    public float getWeightedSentimentScore(String text) {
        try {
            String cleanText = preprocessText(text);

            Document doc = Document.newBuilder()
                    .setContent(cleanText)
                    .setType(Document.Type.PLAIN_TEXT)
                    .build();

            AnalyzeSentimentResponse response = languageServiceClient.analyzeSentiment(doc);
            List<Sentence> sentences = response.getSentencesList();

            if (sentences.isEmpty()) {
                return 0.0f;
            }

            float weightedSum = 0.0f;
            float totalMagnitude = 0.0f;

            for (Sentence sentence : sentences) {
                float score = sentence.getSentiment().getScore();
                float magnitude = sentence.getSentiment().getMagnitude();
                weightedSum += score * magnitude;
                totalMagnitude += magnitude;

                log.debug("문장 감성 분석 - 문장: '{}', 점수: {}, 강도: {}",
                        sentence.getText().getContent(), score, magnitude);
            }

            float finalScore = totalMagnitude == 0.0f ? 0.0f : weightedSum / totalMagnitude;
            log.info("감성 분석 결과: '{}' (최종 점수: {})", cleanText, finalScore);
            return finalScore;

        } catch (Exception e) {
            log.error("감성 분석 중 오류 발생", e);
            return 0.0f;
        }
    }
}
