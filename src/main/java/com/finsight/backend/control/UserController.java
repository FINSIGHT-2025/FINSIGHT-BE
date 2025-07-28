package com.finsight.backend.control;

import com.finsight.backend.dto.request.SignUpRequest;
import com.finsight.backend.service.EmailService;
import com.finsight.backend.service.UserService;
import com.finsight.backend.vo.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/*
회원가입 및 중복 확인 API를 담당
*/
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    // ✅ 1. 회원가입 (POST /users)
    @PostMapping("")
    public ResponseEntity<?> signUp(@RequestBody @Valid SignUpRequest request) {
        // 이메일 인증 여부 확인
        if (!emailService.isEmailVerified(request.getEmail())) {
            return ResponseEntity.badRequest().body("이메일 인증이 완료되지 않았습니다.");
        }

        Member member = Member.builder()
                .userId(request.getUserId())
                .username(request.getUsername())
//                .nickname(request.getNickname())
                .password(request.getPassword())
                .birthday(request.getBirthday())
                .email(request.getEmail())
                .build();

        boolean success = userService.registerMember(member);

        if (success) {
            emailService.removeVerifiedEmail(request.getEmail());  // ✅ 인증 상태 초기화
            return ResponseEntity.ok("회원가입 성공");
        } else {
            return ResponseEntity.badRequest().body("중복된 항목이 있습니다.");
        }
    }


    // ✅ 2. 아이디 / 닉네임 / 이메일 중복 확인 (GET /users?userid= or nickname= or email=)
    @GetMapping("")
    public ResponseEntity<Boolean> checkDuplicates(
            @RequestParam(required = false) String userid,
//            @RequestParam(required = false) String nickname,
            @RequestParam(required = false) String email
    ) {
        if (userid != null) {
            return ResponseEntity.ok(userService.isUserIdTaken(userid));
        }
//        else if (nickname != null) {
//            return ResponseEntity.ok(userService.isNicknameTaken(nickname));
//        }
        else if (email != null) {
            return ResponseEntity.ok(userService.isEmailTaken(email));
        }
        else {
            return ResponseEntity.badRequest().body(false); // 파라미터 없음
        }
    }
}
