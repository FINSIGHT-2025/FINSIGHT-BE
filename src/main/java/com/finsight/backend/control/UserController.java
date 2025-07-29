package com.finsight.backend.control;

import com.finsight.backend.dto.request.LoginForm;
import com.finsight.backend.dto.request.SignupForm;
import com.finsight.backend.dto.response.ApiResponse;
import com.finsight.backend.dto.response.LoginResponseWithToken;
import com.finsight.backend.dto.response.TokenInfoDto;
import com.finsight.backend.enumerate.ErrorCode;
import com.finsight.backend.service.EmailService;
import com.finsight.backend.service.UserService;
import com.finsight.backend.util.JwtUtil;
import com.finsight.backend.vo.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/users")
public class UserController {
    private final UserService UserService;
    private final JwtUtil jwtUtil;
    private final EmailService emailService;

// user123 / securepassword123!
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginForm loginForm){
        Optional<User> optionalUser = UserService.findUser(loginForm);

        if(optionalUser.isEmpty()){
            log.warn("[User.login] NOT_FOUND_USER : {}", loginForm.getUserId());
            return ResponseEntity
                    .status(ErrorCode.NOT_FOUND_USER.getHttpStatus())
                    .body(new ApiResponse<>(Boolean.FALSE, null, ErrorCode.NOT_FOUND_USER.getMessage()));
        }
        User user = optionalUser.get();
        String accessToken = jwtUtil.generateAccessToken(
                new TokenInfoDto(
                        user.getUserId(),
                        user.getUserName()
                )
        );
        return ResponseEntity.ok(new ApiResponse<>(Boolean.TRUE, new LoginResponseWithToken(user.getUserEmail(), user.getUserRole(), accessToken), null));
    }
    @PostMapping("")
    public ResponseEntity<?> signUp(@Valid @RequestBody SignupForm signupForm) {

        // 이메일 인증 여부 확인
        if (!emailService.isEmailVerified(signupForm.getEmail())) {
            log.warn("[User.signup] NOT_AUTH_EMAIL : {}", signupForm.getEmail());
            return ResponseEntity.status(ErrorCode.NOT_AUTH_EMAIL.getHttpStatus()).body(
                    new ApiResponse<>(Boolean.FALSE, null, ErrorCode.NOT_AUTH_EMAIL.getMessage())
            );
        }

        boolean success = UserService.registerUser(signupForm);

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
            @RequestParam(required = false) String userid
    ) {
        if (userid != null) {
            return ResponseEntity.ok(UserService.checkUserId(userid));
        } else {
            return ResponseEntity.badRequest().body(
                    new ApiResponse<>(Boolean.FALSE, "/", "잘못된 URL 접근입니다.")
            );
        }
    }
}
