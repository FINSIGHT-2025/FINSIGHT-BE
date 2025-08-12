package com.finsight.backend.controller;

import com.finsight.backend.dto.request.LoginForm;
import com.finsight.backend.dto.request.SignupForm;
import com.finsight.backend.dto.response.ApiResponse;
import com.finsight.backend.dto.response.LoginResponseWithToken;
import com.finsight.backend.dto.response.TokenInfoDto;
import com.finsight.backend.common.exception.ErrorCode;
import com.finsight.backend.service.AuthService;
import com.finsight.backend.service.EmailService;
import com.finsight.backend.service.UserService;
import com.finsight.backend.common.util.HeaderUtil;
import com.finsight.backend.common.util.JwtUtil;
import com.finsight.backend.domain.vo.user.UserVO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import static com.finsight.backend.common.exception.ErrorCode.NOT_FOUND_USER;

@CrossOrigin(origins = "*") // 또는 특정 포트 명시
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/users")
public class UserController {
    private final AuthService authService;
    private final EmailService emailService;
    private final UserService userService;

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        authService.isValidToken(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginForm loginForm){
        UserVO user = userService.findUser(loginForm);

        String accessToken = authService.generateAccessToken(
                new TokenInfoDto(
                        user.getUserId(),
                        user.getUserName()
                )
        );

        return ResponseEntity.ok(new LoginResponseWithToken(user.getUserEmail(), user.getUserRole(), accessToken));
    }
    @PostMapping("")
    public ResponseEntity<?> signUp(@Valid @RequestBody SignupForm signupForm) {
        emailService.isEmailVerified(signupForm.getEmail());
        userService.registerUser(signupForm);
        return ResponseEntity.ok().build();
    }


    // ✅ 2. 아이디 / 닉네임 / 이메일 중복 확인 (GET /users?userid= or nickname= or email=)
    @GetMapping("")
    public ResponseEntity<?> checkDuplicates(
            @RequestParam(name = "userid") String userId
    ) {
        userService.checkUserId(userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/token")
    public ResponseEntity<?> generateToken(HttpServletRequest request) {
        String newAccessToken = authService.reGenerateAccessToken(request);
        return ResponseEntity.ok(newAccessToken);
    }
    @GetMapping("/me")
    public ResponseEntity<?> getMyInfo(HttpServletRequest request) {
        String userId = authService.isValidToken(request);
        UserVO user = userService.findByUserId(userId);
        Map<String, Object> responseData = Map.of(
                "userId", user.getUserId(),
                "userName", user.getUserName(),
                "userRole", user.getUserRole()
        );

        return ResponseEntity.ok(responseData);
    }
    // ✅ 마이페이지에 개인정보 GET 호출하기
    @GetMapping("/info")
    @ResponseBody
    public ResponseEntity<?> getUsersInfo(HttpServletRequest request) {
        String userId = authService.isValidToken(request);
        UserVO user = userService.findByUserId(userId);
        Map<String, Object> responseData = Map.of(
                "userId", user.getUserId(),
                "userName", user.getUserName(),
                "userEmail", user.getUserEmail(),
                "userBirthday", user.getUserBirthday(),
                "userCreatedAt", user.getUserCreatedAt()
        );
        return ResponseEntity.ok(responseData);
    }

    @DeleteMapping("")
    public ResponseEntity<?> deleteUser(HttpServletRequest request) {
        String userId = authService.isValidToken(request);
        userService.deleteUser(userId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/info")
    public ResponseEntity<?> updateUserInfo(
            @RequestBody Map<String, String> updateData,
            HttpServletRequest request
    ) {
        String userId = authService.isValidToken(request);
        String newEmail = updateData.get("email");
        String newPassword = updateData.get("password");
        userService.updateUserInfo(userId, newPassword, newEmail);

        return ResponseEntity.ok().build();
    }



//    @DeleteMapping("")
//    public ResponseEntity<?> deleteUser(HttpServletRequest request) {
//        Optional<String> token = HeaderUtil.refineHeader(request, "Authorization", "Bearer ");
//        if (token.isEmpty()) {
//            log.warn("[User.deleteUser] NOT_FOUND_TOKEN");
//            return ResponseEntity.status(ErrorCode.NOT_FOUND_TOKEN.getHttpStatus())
//                    .body(new ApiResponse<>(false, null, ErrorCode.NOT_FOUND_TOKEN.getMessage()));
//        }
//        try {
//            Claims claims = jwtUtil.validateToken(token.get());
//            String userId = claims.get("userId", String.class);
//            userService.deleteUser(userId);
//            return ResponseEntity.ok(new ApiResponse<>(true, null, "탈퇴 성공"));
//        } catch (ExpiredJwtException e) {
//            return ResponseEntity.status(ErrorCode.EXPIRED_TOKEN_ERROR.getHttpStatus())
//                    .body(new ApiResponse<>(false, null, ErrorCode.EXPIRED_TOKEN_ERROR.getMessage()));
//        } catch (JwtException e) {
//            return ResponseEntity.status(ErrorCode.NOT_TOKEN_INVALID.getHttpStatus())
//                    .body(new ApiResponse<>(false, null, ErrorCode.NOT_TOKEN_INVALID.getMessage()));
//        } catch (Exception e) {
//            log.error("[User.deleteUser] 탈퇴 중 서버 오류", e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(new ApiResponse<>(false, null, "회원 탈퇴 중 오류가 발생했습니다."));
//        }
//    }
}