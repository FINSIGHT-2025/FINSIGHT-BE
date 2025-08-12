package com.finsight.backend.controller;

import com.finsight.backend.dto.request.EmailRequest;
import com.finsight.backend.dto.request.VerifyCodeRequest;
import com.finsight.backend.service.EmailService;
import com.finsight.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

/*
이메일 인증과 관련된 요청을 처리하는 Spring MVC 컨트롤러
사용자 요청을 받아서 EmailService에게 일을 시키는 입구 역할

역할: 프론트엔드 → 백엔드 요청을 받아 EmailService에 전달
장점: 서비스 로직과 분리되어 있어 테스트와 유지보수가 편리함
비유: 사용자가 전화를 걸면 컨트롤러가 받고, 실무자(서비스)에게 연결해주는 구조
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class EmailController {
    private final EmailService emailService;

    private final UserService userService;

    // ✅ 이메일 인증 코드 요청 (중복 확인 포함)
    @PostMapping("/email")
    public ResponseEntity<?> sendEmail(@RequestBody EmailRequest req) {
        String email = req.getEmail();

        // 1. 중복된 이메일이면 차단
        userService.checkEmail(email);

        // 2. 인증 코드 전송
        emailService.sendVerificationCode(email);
        return ResponseEntity.ok(Collections.singletonMap("message", "인증 코드가 전송되었습니다."));
    }

    // ✅ 인증 코드 확인
    @PostMapping("/authcode")
    public ResponseEntity<?> verify(@RequestBody VerifyCodeRequest req) {
        log.info("/authcode 호출");
        boolean verified = emailService.verifyCode(req.getEmail(), req.getCode());

        if (verified) {
            emailService.removeCode(req.getEmail()); // 인증 성공 시 코드 삭제
            return ResponseEntity.ok(Collections.singletonMap("verified", true));
        } else {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonMap("verified", false));
        }
    }
}