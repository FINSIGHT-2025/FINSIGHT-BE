package com.finsight.backend.control;

import com.finsight.backend.dto.request.LoginForm;
import com.finsight.backend.dto.request.SignUpRequest;
import com.finsight.backend.dto.request.SignupForm;
import com.finsight.backend.dto.response.ApiResponse;
import com.finsight.backend.dto.response.LoginResponseWithToken;
import com.finsight.backend.dto.response.TokenInfoDto;
import com.finsight.backend.enumerate.ErrorCode;
import com.finsight.backend.service.EmailService;
import com.finsight.backend.service.MemberService;
import com.finsight.backend.util.JwtUtil;
import com.finsight.backend.vo.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/users")
public class MemberController {
    private final MemberService memberService;
    private final JwtUtil jwtUtil;
    private final EmailService emailService;

// user123 / securepassword123!
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginForm loginForm){
        Optional<Member> optionalMember = memberService.findMember(loginForm);

        if(optionalMember.isEmpty()){
            log.warn("[Member.login] NOT_FOUND_USER : {}", loginForm.getUserId());
            return ResponseEntity
                    .status(ErrorCode.NOT_FOUND_USER.getHttpStatus())
                    .body(new ApiResponse<>(Boolean.FALSE, null, ErrorCode.NOT_FOUND_USER.getMessage()));
        }
        Member member = optionalMember.get();
        String accessToken = jwtUtil.generateAccessToken(
                new TokenInfoDto(
                        member.getUserId(),
                        member.getUsername(),
                        member.getNickname()
                )
        );
        return ResponseEntity.ok(new ApiResponse<>(Boolean.TRUE, new LoginResponseWithToken(member.getEmail(), member.getRole(), accessToken), null));
    }
    @PostMapping("")
    public ResponseEntity<?> signUp(@Valid @RequestBody SignupForm signupForm) {

        // 이메일 인증 여부 확인
        if (!emailService.isEmailVerified(signupForm.getEmail())) {
            log.warn("[Member.signup] NOT_AUTH_EMAIL : {}", signupForm.getEmail());
            return ResponseEntity.status(ErrorCode.NOT_AUTH_EMAIL.getHttpStatus()).body(
                    new ApiResponse<>(Boolean.FALSE, null, ErrorCode.NOT_AUTH_EMAIL.getMessage())
            );
        }

        boolean success = memberService.registerMember(signupForm);

        if (success) {
            emailService.removeVerifiedEmail(signupForm.getEmail());  // ✅ 인증 상태 초기화
            return ResponseEntity.ok(new ApiResponse<>(Boolean.TRUE, null, null));
        } else {
            return ResponseEntity.status(ErrorCode.DUPLICATE_USER.getHttpStatus()).body(
                    new ApiResponse<>(Boolean.FALSE, null, ErrorCode.DUPLICATE_USER.getMessage())
            );
        }
    }


    // ✅ 2. 아이디 / 닉네임 / 이메일 중복 확인 (GET /users?userid= or nickname= or email=)
    @GetMapping("")
    public ResponseEntity<?> checkDuplicates(
            @RequestParam(required = false) String userid,
            @RequestParam(required = false) String nickname
    ) {
        if (userid != null) {
            return ResponseEntity.ok(memberService.checkUserId(userid));
        }
        if (nickname != null) {
            return ResponseEntity.ok(memberService.checkNickname(nickname));
        } else {
            return ResponseEntity.badRequest().body(
                    new ApiResponse<>(Boolean.FALSE, "/", "잘못된 URL 접근입니다.")
            );
        }
    }
}
