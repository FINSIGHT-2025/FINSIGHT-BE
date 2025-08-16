package com.finsight.backend.controller;

import com.finsight.backend.dto.WatchListDTO;
import com.finsight.backend.common.exception.InvTestException;
import com.finsight.backend.service.TradeService;
import com.finsight.backend.service.WatchListService;
import com.finsight.backend.common.util.HeaderUtil;
import com.finsight.backend.common.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    // 찜한 상품 등록
    @PostMapping("")
    public ResponseEntity<String> insertWatch(@RequestBody WatchListDTO watchListDTO, HttpServletRequest request) {
        String accessToken = extractAccessToken(request);
        watchListService.insertWatch(watchListDTO, accessToken);

        return ResponseEntity.ok("찜한 상품 둥록 완료");
    }

    // 찜한 상품 해제
    @DeleteMapping("/{category}/{code}")
    public ResponseEntity<String> deleteWatch(@PathVariable String category,
                                              @PathVariable String code,
                                              HttpServletRequest request) {
        String accessToken = extractAccessToken(request);
        watchListService.deleteWatch(category, code, accessToken);

        return ResponseEntity.ok("찜한 상품 해제 완료");
    }


    private String extractAccessToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            throw new RuntimeException("Authorization 헤더가 없거나 형식이 올바르지 않습니다.");
        }
        return header.substring(7);
    }
}
