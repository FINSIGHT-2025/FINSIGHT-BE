package com.finsight.backend.controller;

import com.finsight.backend.dto.InvTestDTO;
import com.finsight.backend.common.exception.InvTestException;
import com.finsight.backend.service.InvTestService;
import com.finsight.backend.common.util.HeaderUtil;
import com.finsight.backend.common.util.JwtUtil; // JwtUtil 임포트 (필수)
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users/invt")
public class InvTestController {
    @Autowired
    private InvTestService invTestService;

    @Autowired
    private JwtUtil jwtUtil;
    @GetMapping("")
    public ResponseEntity<Map<String, String>> getInvestmentProfile(HttpServletRequest request) { // String -> Map<String, String>으로 변경
        try {
            String accessToken = HeaderUtil.refineHeader(request, "Authorization", "Bearer ")
                    .orElseThrow(() -> new InvTestException("인증 토큰이 필요합니다.", HttpStatus.UNAUTHORIZED)); // 401 UNAUTHORIZED

            String userId;
            try {
                Claims claims = jwtUtil.validateToken(accessToken);
                userId = claims.get("userId", String.class);
                if (userId == null) {
                    throw new JwtException("토큰에 사용자 ID 정보가 없습니다.");
                }
            } catch (JwtException e) {
                System.err.println("[에러] JWT 검증 실패 (GET): " + e.getMessage());
                throw new InvTestException("유효하지 않거나 만료된 토큰입니다.", HttpStatus.FORBIDDEN); // 403 FORBIDDEN
            }

            String investmentProfileType = invTestService.getInvestmentProfileTypeByUserId(userId);
            String investmentProfileUpdatedAt = invTestService.getInvestmentProfileUpdatedAt(userId);

            if (investmentProfileType == null) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("message", "사용자의 투자 성향 정보를 찾을 수 없습니다.");
                return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND); // 404 NOT FOUND
            }
            if (investmentProfileUpdatedAt == null) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("message", "사용자의 갱신일자 정보를 찾을 수 없습니다.");
                return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND); // 404 NOT FOUND
            }

            Map<String, String> successResponse = new HashMap<>();
            successResponse.put("investmentProfileType", investmentProfileType);
            successResponse.put("investmentProfileUpdatedAt", investmentProfileUpdatedAt);
            return new ResponseEntity<>(successResponse, HttpStatus.OK);

        } catch (InvTestException e) {
            System.err.println("[에러] 투자 성향 조회 중 InvTestException 발생: " + e.getMessage());
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", e.getMessage());
            return new ResponseEntity<>(errorResponse, e.getHttpStatus()); // 예외에 포함된 HttpStatus 반환
        } catch (Exception e) {
            System.err.println("[에러] 투자 성향 조회 중 예상치 못한 에러 발생: " + e.getMessage());
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "서버 내부 오류가 발생했습니다.");
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR); // 500 INTERNAL SERVER ERROR
        }
    }

    @PutMapping("")
    public ResponseEntity<String> upsertInvTest(@RequestBody InvTestDTO invTestDTO, HttpServletRequest request) {
        String accessToken = extractAccessToken(request);
        invTestService.upsertInvTest(invTestDTO, accessToken);
        return ResponseEntity.ok("투자성향 수정 완료");
    }
    private String extractAccessToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            throw new RuntimeException("Authorization 헤더가 없거나 형식이 올바르지 않습니다.");
        }
        return header.substring(7);
    }
}