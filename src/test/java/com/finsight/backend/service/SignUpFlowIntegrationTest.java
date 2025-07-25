package com.finsight.backend;

import com.finsight.backend.config.RootConfig;
import com.finsight.backend.service.EmailService;
import com.finsight.backend.service.UserService;
import com.finsight.backend.vo.Member;
import com.finsight.backend.enumerate.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitConfig(classes = RootConfig.class)
public class SignUpFlowIntegrationTest {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserService userService;

    private final String testEmail = "testuser@example.com";
    private final String testUserId = "testuser01";
    private final String testNickname = "testnickname";

    @BeforeEach
    void cleanup() {
        emailService.removeCode(testEmail);
        emailService.removeVerifiedEmail(testEmail);
    }

    @Test
    @DisplayName("1. 이메일 인증 후 회원가입이 성공한다")
    void fullSignupFlow_Success() {
        // 1. 이메일 인증 코드 발송
        emailService.sendVerificationCode(testEmail);
        assertTrue(true, "인증 코드 발송 완료");

        // 2. 코드 저장 여부 확인
        String code = extractCodeForTest(testEmail); // 테스트 전용 내부 접근 메서드
        assertNotNull(code);

        // 3. 코드 검증 성공
        boolean verified = emailService.verifyCode(testEmail, code);
        assertTrue(verified, "이메일 인증 성공");

        // 4. 회원가입 요청
        Member member = Member.builder()
                .userId(testUserId)
                .username("홍길동")
                .nickname(testNickname)
                .password("plaintextpw")
                .birthday(LocalDate.of(1990, 1, 1))
                .email(testEmail)
                .build();

        boolean registered = userService.registerMember(member);
        assertTrue(registered, "회원가입 성공");

        // 5. 인증된 이메일에서 제거되었는지 확인
        assertFalse(emailService.isEmailVerified(testEmail), "가입 후 인증된 이메일 제거됨");
    }

    @Test
    @DisplayName("2. 인증되지 않은 이메일로는 회원가입 불가")
    void signupFails_WithoutEmailVerification() {
        Member member = Member.builder()
                .userId("unauthuser")
                .username("임의사용자")
                .nickname("unauthnick")
                .password("pass1234")
                .birthday(LocalDate.of(1999, 5, 5))
                .email("unauth@example.com")
                .build();

        boolean registered = userService.registerMember(member);
        assertFalse(registered, "이메일 인증 없이 가입 시도 실패");
    }

    // ✅ 테스트 편의용 코드: 실제 운영 코드에는 포함 X
    private String extractCodeForTest(String email) {
        try {
            var field = EmailService.class.getDeclaredField("codeStorage");
            field.setAccessible(true);
            @SuppressWarnings("unchecked")
            Map<String, ?> map = (Map<String, ?>) field.get(emailService);
            Object ev = map.get(email);
            var codeField = ev.getClass().getDeclaredField("code");
            codeField.setAccessible(true);
            return (String) codeField.get(ev);
        } catch (Exception e) {
            throw new RuntimeException("테스트용 코드 추출 실패", e);
        }
    }
}
