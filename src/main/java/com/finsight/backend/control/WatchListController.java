package com.finsight.backend.control;

import com.finsight.backend.dto.WatchListDTO;
import com.finsight.backend.exception.InvTestException;
import com.finsight.backend.service.TradeService;
import com.finsight.backend.service.WatchListService;
import com.finsight.backend.util.HeaderUtil;
import com.finsight.backend.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/watchs")
public class WatchListController {
    @Autowired
    private WatchListService watchListService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private TradeService tradeService;

    @GetMapping("/deposit")
    public ResponseEntity<?> getWatchListDepositByUserId(HttpServletRequest request) {
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

            List<WatchListDTO> watchList = watchListService.getWatchListDepositByUserId(userId);
            return ResponseEntity.ok(watchList);
        } catch (Exception e) {
            System.err.println("[에러] watchList 조회 중 Exception 발생: " + e.getMessage());

            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }
}
