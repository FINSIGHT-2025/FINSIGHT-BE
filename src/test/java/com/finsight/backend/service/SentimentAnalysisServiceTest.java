package com.finsight.backend.service;

import com.finsight.backend.config.GcpConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {GcpConfig.class, SentimentAnalysisService.class})
@Tag("integration")
class SentimentAnalysisServiceIntegrationTest {

    @Autowired
    private SentimentAnalysisService sentimentAnalysisService;

    @Test
    @DisplayName("String 리스트를 사용하여 실제 감성 분석 API를 호출하고 결과를 출력한다")
    void runRealApiCallWithListOfStrings() {
        // 1. ✨ 테스트할 문장들을 String 리스트로 직접 정의합니다.
        List<String> testSentences = List.of(
                "더블유씨피, 2분기 260억 적자…\"시장 수요 회복\""
        );

        System.out.println("--- Starting Real API Call Test with a List ---");

        // 2. ✨ 리스트의 각 문장에 대해 반복 실행합니다.
        testSentences.forEach(text -> {
            // when: 실제 서비스 메서드를 호출합니다.
            float score = sentimentAnalysisService.getWeightedSentimentScore(text);

            // then: 결과를 콘솔에 출력합니다.
            System.out.printf("Text: [%s] --> Score: [%.3f]%n", text, score);


            float score2 = sentimentAnalysisService.getSentimentScore(text);
            System.out.printf("Text: [%s] --> Score2: [%.3f]%n", text, score);

        });

        System.out.println("--- Test Finished ---");
    }
}