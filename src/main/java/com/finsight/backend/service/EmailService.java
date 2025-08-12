package com.finsight.backend.service;

import com.finsight.backend.common.exception.user.CustomEmailNotVerifiedException;
import com.finsight.backend.domain.vo.user.EmailVerificationVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.security.SecureRandom;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/*
이메일 인증 로직의 핵심 기능을 처리하는 서비스 클래스
즉, 실제로 인증 코드를 생성하고, 저장하고, 이메일로 발송하고, 인증 여부를 검증하는 실무자 역할

사용자의 이메일에 인증 코드를 발송하고,
해당 코드를 검증하고,
만료된 코드는 자동으로 제거하는 기능까지 담당
 */
@Slf4j
@Service
public class EmailService {

    private final Map<String, EmailVerificationVO> codeStorage = new ConcurrentHashMap<>();
    private final Set<String> verifiedEmails = ConcurrentHashMap.newKeySet();  // ✅ 인증 완료 이메일 저장소
    private final Random random = new SecureRandom();

    @Autowired
    private JavaMailSender mailSender;

    public void sendVerificationCode(String email) {
        String code = String.format("%06d", random.nextInt(1000000));
        long expireAt = System.currentTimeMillis() + (5 * 60 * 1000);

        codeStorage.remove(email); // 기존 인증코드 제거
        codeStorage.put(email, new EmailVerificationVO(code, expireAt)); // 새 코드 저장

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("[Fin-Sight] 이메일 인증 코드입니다.");
        message.setText("인증 코드: " + code + "\n5분 내로 입력해주세요.");
        mailSender.send(message);
    }

    public boolean verifyCode(String email, String code) {
        EmailVerificationVO v = codeStorage.get(email);
        if (v == null || v.isExpired()) {
            codeStorage.remove(email);
            return false;
        }

        if (v.getCode().equals(code)) {
            codeStorage.remove(email);               // 인증 코드 제거
            verifiedEmails.add(email);               // ✅ 인증된 이메일 저장
            return true;
        }
        return false;
    }

    public void isEmailVerified(String email) {
        boolean isCheckEmail = verifiedEmails.contains(email);// ✅ 회원가입 시 인증 여부 확인용
        if(!isCheckEmail) {
            log.warn("[EmailService] Not Verified Email");
            throw new CustomEmailNotVerifiedException();
        }
    }

    public void removeVerifiedEmail(String email) {
        verifiedEmails.remove(email);                 // ✅ 회원가입 완료 후 삭제
    }

    public void removeCode(String email) {
        codeStorage.remove(email);
    }
}

//@Service
//public class EmailService {
//
//    private final Map<String, EmailVerification> codeStorage = new ConcurrentHashMap<>();
//    private final Random random = new SecureRandom();
//
//    @Autowired
//    private JavaMailSender mailSender;
//
//    public void sendVerificationCode(String email) {
//        String code = String.format("%06d", random.nextInt(1000000));
//        long expireAt = System.currentTimeMillis() + (5 * 60 * 1000);
//
//        codeStorage.remove(email); // 기존 인증코드 제거 (명시적)
//        codeStorage.put(email, new EmailVerification(code, expireAt)); // 새 코드 저장
//
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo(email);
//        message.setSubject("[Fin-Sight] 이메일 인증 코드입니다.");
//        message.setText("인증 코드: " + code + "\n5분 내로 입력해주세요.");
//        mailSender.send(message);
//    }
//
//    public boolean verifyCode(String email, String code) {
//        EmailVerification v = codeStorage.get(email);
//        if (v == null || v.isExpired()) {
//            codeStorage.remove(email);
//            return false;
//        }
//
//        if (v.getCode().equals(code)) {
//            codeStorage.remove(email);  // 인증 성공 시 자동 삭제
//            return true;
//        }
//        return false;
//    }
//
//    public void removeCode(String email) {
//        codeStorage.remove(email);
//    }
//}

