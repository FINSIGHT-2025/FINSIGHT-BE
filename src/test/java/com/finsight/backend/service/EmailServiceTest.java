package com.finsight.backend.service;

import com.finsight.backend.config.RootConfig;
import com.finsight.backend.service.EmailService;
import com.finsight.backend.vo.EmailVerification;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.lang.reflect.Field;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitConfig(classes = RootConfig.class)
public class EmailServiceTest {

    @Autowired
    private EmailService emailService;

    @Test
    public void testSendEmail() {
        String testEmail = "test@naver.com";
        emailService.sendVerificationCode(testEmail);
        System.out.println("✅ 인증 코드 이메일이 전송되었습니다.");
    }

    @Test
    public void testVerifyCorrectCode() throws Exception {
        String testEmail = "test@example.com";
        emailService.sendVerificationCode(testEmail);

        // private 필드 codeStorage 접근
        Field codeStorageField = EmailService.class.getDeclaredField("codeStorage");
        codeStorageField.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, EmailVerification> codeStorage = (Map<String, EmailVerification>) codeStorageField.get(emailService);

        String correctCode = codeStorage.get(testEmail).getCode();
        boolean result = emailService.verifyCode(testEmail, correctCode);
        assertTrue(result, "올바른 코드일 경우 인증에 성공해야 합니다.");
    }

    @Test
    public void testVerifyWrongCode() {
        String testEmail = "test@example.com";
        emailService.sendVerificationCode(testEmail);

        boolean result = emailService.verifyCode(testEmail, "000000");
        assertFalse(result, "잘못된 코드일 경우 인증에 실패해야 합니다.");
    }

    @Test
    public void testVerifyExpiredCode() throws Exception {
        String testEmail = "test@example.com";

        // 직접 만료된 인증 코드 삽입
        Field codeStorageField = EmailService.class.getDeclaredField("codeStorage");
        codeStorageField.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, EmailVerification> codeStorage = (Map<String, EmailVerification>) codeStorageField.get(emailService);
        codeStorage.put(testEmail, new EmailVerification("123456", System.currentTimeMillis() - 1)); // 만료된 시간

        boolean result = emailService.verifyCode(testEmail, "123456");
        assertFalse(result, "만료된 코드는 인증에 실패해야 합니다.");
    }
}
