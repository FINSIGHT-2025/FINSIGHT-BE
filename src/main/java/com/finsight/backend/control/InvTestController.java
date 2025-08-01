// 2025-07-29 JY
package com.finsight.backend.control;

import com.finsight.backend.dto.InvTestDTO;
import com.finsight.backend.exception.InvTestException;
import com.finsight.backend.service.InvTestService;
import com.finsight.backend.util.HeaderUtil;
import com.finsight.backend.util.JwtUtil; // JwtUtil 임포트 (필수)
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController // Restful API Controller임을 선언함.
@RequestMapping("/users/invt")// URL 경로 설정
public class InvTestController {
    @Autowired // Service 주입
    private InvTestService invTestService;

    @Autowired // JwtUtil 주입 (새로 추가)
    private JwtUtil jwtUtil;

    @PutMapping("") // json 형식으로 user_id, risk_type을 받음
    public ResponseEntity<String> insertInvTest(@RequestBody InvTestDTO invTestDTO, HttpServletRequest request) {
        try {
            String accessToken = HeaderUtil.refineHeader(request, "Authorization", "Bearer ")
                    .orElseThrow(() -> new InvTestException("인증 토큰이 필요합니다.", HttpStatus.UNAUTHORIZED)); // InvTestException 사용

            // 서비스 계층에서 토큰 검증 및 userId 추출을 하므로, 여기서는 바로 서비스 호출
            invTestService.insertInvTest(invTestDTO, accessToken);

            return new ResponseEntity<>("[성공] InvestmentProfile 테이블에 성공적으로 INSERT 되었습니다.",HttpStatus.OK);
        } catch (InvTestException e) { // InvTestException을 우선 처리
            System.err.println("[에러] InvestmentProfile 테이블에 INSERT 중 InvTestException 발생: " + e.getMessage());
            return new ResponseEntity<>(e.getMessage(), e.getHttpStatus());
        } catch (Exception e) {
            System.err.println("[에러] InvestmentProfile 테이블에 INSERT 도중 예상치 못한 에러가 발생했습니다." + e.getMessage());
            return new ResponseEntity<>("서버 내부 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // --- 새로 추가된 GET 요청 처리 메서드 ---
    @GetMapping("") // 투자 성향 조회 (JWT 토큰에서 사용자 ID 추출)
    public ResponseEntity<Map<String, String>> getInvestmentProfile(HttpServletRequest request) { // String -> Map<String, String>으로 변경
        try {
            // 1. HeaderUtil을 사용하여 Access Token 추출
            String accessToken = HeaderUtil.refineHeader(request, "Authorization", "Bearer ")
                    .orElseThrow(() -> new InvTestException("인증 토큰이 필요합니다.", HttpStatus.UNAUTHORIZED)); // 401 UNAUTHORIZED

            // 2. JwtUtil을 주입받아 JWT 토큰 검증 및 사용자 ID 추출
            String userId;
            try {
                // JwtUtil 인스턴스를 통해 validateToken 호출 (수정)
                Claims claims = jwtUtil.validateToken(accessToken);
                userId = claims.get("userId", String.class); // JWT 생성 시 사용했던 클레임 이름으로 userId 추출
                if (userId == null) {
                    throw new JwtException("토큰에 사용자 ID 정보가 없습니다.");
                }
            } catch (JwtException e) {
                // 토큰 만료, 서명 오류 등 JWT 관련 예외 처리
                System.err.println("[에러] JWT 검증 실패 (GET): " + e.getMessage());
                // InvTestException을 사용하여 HttpStatus를 함께 반환
                throw new InvTestException("유효하지 않거나 만료된 토큰입니다.", HttpStatus.FORBIDDEN); // 403 FORBIDDEN
            }

            // 3. Service 계층을 호출하여 투자 성향 정보 조회
            String investmentProfileType = invTestService.getInvestmentProfileTypeByUserId(userId);

            // 4. 조회 결과에 따라 응답 구성
            if (investmentProfileType == null) {
                // 데이터베이스에 해당 사용자의 투자 성향 정보가 없는 경우
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("message", "사용자의 투자 성향 정보를 찾을 수 없습니다.");
                return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND); // 404 NOT FOUND
            }

            // 5. 성공 응답 (200 OK)
            Map<String, String> successResponse = new HashMap<>();
            successResponse.put("investmentProfileType", investmentProfileType); // 프론트엔드가 기대하는 키
            return new ResponseEntity<>(successResponse, HttpStatus.OK);

        } catch (InvTestException e) {
            // 사용자 정의 예외 (InvTestException) 처리
            System.err.println("[에러] 투자 성향 조회 중 InvTestException 발생: " + e.getMessage());
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", e.getMessage());
            return new ResponseEntity<>(errorResponse, e.getHttpStatus()); // 예외에 포함된 HttpStatus 반환
        } catch (Exception e) {
            // 그 외 예상치 못한 일반 예외 처리
            System.err.println("[에러] 투자 성향 조회 중 예상치 못한 에러 발생: " + e.getMessage());
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "서버 내부 오류가 발생했습니다.");
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR); // 500 INTERNAL SERVER ERROR
        }
    }
}