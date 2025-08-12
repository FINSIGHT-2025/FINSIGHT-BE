package com.finsight.backend.controller;

import com.finsight.backend.config.*;
import com.finsight.backend.service.UserViewLogger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        RootConfig.class,
        MongoConfig.class,
        UserViewLogger.class,
        ServletConfig.class,
})
@WebAppConfiguration
public class RecommendationControllerTest {

    private static final String BASE_URL = "http://localhost:8080"; // 서버 실행 중이어야 함
    private final RestTemplate restTemplate = new RestTemplate();

    @Test
    @DisplayName("🔐 로그인 후 추천 API 호출")
    void recommendApi_withValidToken_shouldReturnRecommendations() {
        // 1️⃣ 로그인 요청 → JWT 토큰 획득
        String loginUrl = BASE_URL + "/users/login";

        HttpHeaders loginHeaders = new HttpHeaders();
        loginHeaders.setContentType(MediaType.APPLICATION_JSON);

        String loginBody = """
            {
              "userId": "user123",
              "password": "1234"
            }
        """;

        HttpEntity<String> loginRequest = new HttpEntity<>(loginBody, loginHeaders);
        ResponseEntity<Map> loginResponse = restTemplate.postForEntity(loginUrl, loginRequest, Map.class);

        assertEquals(HttpStatus.OK, loginResponse.getStatusCode(), "로그인 실패!");

        String accessToken = (String) loginResponse.getBody().get("accessToken");
        assertNotNull(accessToken, "accessToken이 없습니다!");

        // 2️⃣ 추천 API 호출
        String recommendUrl = BASE_URL + "/recommendations?top=5";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken); // Authorization: Bearer ...
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<String> recommendResponse = restTemplate.exchange(
                recommendUrl,
                HttpMethod.GET,
                request,
                String.class
        );

        assertEquals(HttpStatus.OK, recommendResponse.getStatusCode(), "추천 API 응답 실패!");
        System.out.println("💡 추천 결과: " + recommendResponse.getBody());
    }
}
