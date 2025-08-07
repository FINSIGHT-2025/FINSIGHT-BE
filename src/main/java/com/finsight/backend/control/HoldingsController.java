package com.finsight.backend.control;

import com.finsight.backend.dto.request.TradeRequest;
import com.finsight.backend.exception.InvTestException;
import com.finsight.backend.service.HoldingsService;
import com.finsight.backend.service.TradeService;
import com.finsight.backend.util.HeaderUtil;
import com.finsight.backend.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import scala.Int;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/holdings")
public class HoldingsController {

    @Autowired
    private TradeService tradeService;
    @Autowired
    private HoldingsService holdingsService;
    @Autowired
    private JwtUtil jwtUtil;


    @PostMapping("/purchases")
    public ResponseEntity<String> purchase(@RequestBody TradeRequest request) {
        tradeService.processTrade(request, "buy");
        return ResponseEntity.ok("매수 완료");
    }

    @PostMapping("/sales")
    public ResponseEntity<String> sell(@RequestBody TradeRequest request) {
        tradeService.processTrade(request, "sell");
        return ResponseEntity.ok("매도 완료");
    }

    @GetMapping("/deposit")
    public ResponseEntity<?> getDeposit(HttpServletRequest request) {
        try {
            String accessToken = HeaderUtil.refineHeader(request, "Authorization", "Bearer ")
                    .orElseThrow(() -> new InvTestException("인증 토큰이 필요합니다.", HttpStatus.UNAUTHORIZED));

            String userId;
            try {
                Claims claims = jwtUtil.validateToken(accessToken);
                userId = claims.get("userId", String.class);
                if (userId == null) {
                    throw new JwtException("토큰에 사용자 ID 정보가 없습니다.");
                }
            } catch (JwtException e) {
                System.err.println("[에러] JWT 검증 실패 (GET): " + e.getMessage());
                throw new InvTestException("유효하지 않거나 만료된 토큰입니다.", HttpStatus.FORBIDDEN);
            }

            Double depositPrice = holdingsService.getDepositPriceByUserId(userId);
            if (depositPrice == null) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("message", "사용자의 예금 보유 정보를 찾을 수 없습니다.");
                return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
            }

            Map<String, Object> successResponse = new HashMap<>();
            successResponse.put("depositPrice", depositPrice);
            return new ResponseEntity<>(successResponse, HttpStatus.OK);

        } catch (Exception e) {
            System.err.println("[에러] 조회 중 Exception 발생: " + e.getMessage());

            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @GetMapping("/mp")
    public ResponseEntity<?> getDomesticEquity2(HttpServletRequest request) {
        try {
            String accessToken = HeaderUtil.refineHeader(request, "Authorization", "Bearer ")
                    .orElseThrow(() -> new InvTestException("인증 토큰이 필요합니다.", HttpStatus.UNAUTHORIZED));

            String userId;
            try {
                Claims claims = jwtUtil.validateToken(accessToken);
                userId = claims.get("userId", String.class);
                if (userId == null) {
                    throw new JwtException("토큰에 사용자 ID 정보가 없습니다.");
                }
            } catch (JwtException e) {
                System.err.println("[에러] JWT 검증 실패 (GET): " + e.getMessage());
                throw new InvTestException("유효하지 않거나 만료된 토큰입니다.", HttpStatus.FORBIDDEN);
            }

            Double domesticEquity2 = holdingsService.getDomesticEquity2ByUserId(userId);
            if (domesticEquity2 == null) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("message", "domesticEquity2 정보를 찾을 수 없습니다.");
                return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
            }
            Double domesticMixed5 = holdingsService.getDomesticMixed5ByUserId(userId);
            if (domesticMixed5 == null) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("message", "domesticMixed5 정보를 찾을 수 없습니다.");
                return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
            }
            Double domesticBond5 = holdingsService.getBond5ByUserId(userId);
            if (domesticBond5 == null) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("message", "domesticBond5 정보를 찾을 수 없습니다.");
                return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
            }
            Double domesticBond6 = holdingsService.getBond6ByUserId(userId);
            if (domesticBond6 == null) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("message", "domesticBond6 정보를 찾을 수 없습니다.");
                return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
            }

            Double foreignEquity1 = holdingsService.getForeignEquity1ByUserId(userId);
            if (foreignEquity1 == null) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("message", "foreignEquity1 정보를 찾을 수 없습니다.");
                return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
            }
            Double foreignEquity2 = holdingsService.getForeignEquity2ByUserId(userId);
            if (foreignEquity2 == null) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("message", "foreignEquity2 정보를 찾을 수 없습니다.");
                return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
            }
            Double foreignEquity3 = holdingsService.getForeignEquity3ByUserId(userId);
            if (foreignEquity3 == null) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("message", "foreignEquity3 정보를 찾을 수 없습니다.");
                return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
            }
            Double foreignBond4 = holdingsService.getForeignBond4ByUserId(userId);
            if (foreignBond4 == null) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("message", "foreignBond4 정보를 찾을 수 없습니다.");
                return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
            }
            //
            Map<String, Object> successResponse = new HashMap<>();
            successResponse.put("domesticEquity2", domesticEquity2);
            successResponse.put("domesticMixed5", domesticMixed5);
            successResponse.put("domesticBond5", domesticBond5);
            successResponse.put("domesticBond6", domesticBond6);

            successResponse.put("foreignEquity1", foreignEquity1);
            successResponse.put("foreignEquity2", foreignEquity2);
            successResponse.put("foreignEquity3", foreignEquity3);
            successResponse.put("foreignBond4", foreignBond4);

            return new ResponseEntity<>(successResponse, HttpStatus.OK);

        } catch (Exception e) {
            System.err.println("[에러] 조회 중 Exception 발생: " + e.getMessage());

            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }
}
