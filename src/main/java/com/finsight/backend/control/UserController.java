package com.finsight.backend.control;

import com.finsight.backend.dto.request.LoginForm;
import com.finsight.backend.dto.request.SignupForm;
import com.finsight.backend.dto.response.ApiResponse;
import com.finsight.backend.dto.response.LoginResponseWithToken;
import com.finsight.backend.dto.response.TokenInfoDto;
import com.finsight.backend.enumerate.ErrorCode;
import com.finsight.backend.security.JwtAuthenticationProvider;
import com.finsight.backend.service.EmailService;
import com.finsight.backend.service.UserService;
import com.finsight.backend.util.HeaderUtil;
import com.finsight.backend.util.JwtUtil;
import com.finsight.backend.vo.User;
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
import java.util.Enumeration;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "*") // 또는 특정 포트 명시
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/users")
public class UserController {
    private final UserService UserService;
    private final JwtUtil jwtUtil;
    private final EmailService emailService;
    private final UserService userService;

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

    @PostMapping("/token")
    public ResponseEntity<?> generateToken(HttpServletRequest request) throws ServletException, IOException {
        Enumeration<String> headerNames = request.getHeaderNames();

        headerNames.asIterator()
                .forEachRemaining(headerName -> {
                    String headerValue = request.getHeader(headerName);
                    System.out.println(headerValue + " : " + headerName);
                });

        Optional<String> token = HeaderUtil.refineHeader(request, "Authorization", "Bearer ");

        if(token.isEmpty()){
            log.warn("[User.generateToken] NOT_FOUND_TOKEN");
            return ResponseEntity.status(ErrorCode.NOT_FOUND_TOKEN.getHttpStatus()).body(
                    new ApiResponse<>(Boolean.FALSE, null, ErrorCode.NOT_FOUND_TOKEN.getMessage()));
        }

        try{
            log.info("[User.generateToken] Success Generate Token");
            Claims claims = jwtUtil.validateToken(token.get());
            TokenInfoDto dto = new TokenInfoDto(
                    claims.get("userId", String.class),
                    claims.get("username", String.class)
            );
            String newAccessToken = jwtUtil.generateAccessToken(dto);
            return ResponseEntity.ok(new ApiResponse<>(Boolean.TRUE, newAccessToken, null));
        } catch (ExpiredJwtException e){
            log.warn("[User.generateToken] EXPIRED_TOKEN_ERROR");
            return ResponseEntity.status(ErrorCode.EXPIRED_TOKEN_ERROR.getHttpStatus()).body(
                    new ApiResponse<>(Boolean.FALSE, null, ErrorCode.EXPIRED_TOKEN_ERROR.getMessage())
            );
        } catch (JwtException e){
            log.warn("[User.generateToken] NOT_TOKEN_INVALID");
            return ResponseEntity.status(ErrorCode.NOT_TOKEN_INVALID.getHttpStatus()).body(
                    new ApiResponse<>(Boolean.FALSE, null, ErrorCode.NOT_TOKEN_INVALID.getMessage())
            );
        }
    }
    @GetMapping("/me")
    public ResponseEntity<?> getMyInfo(HttpServletRequest request) {
        Optional<String> token = HeaderUtil.refineHeader(request, "Authorization", "Bearer ");
        if (token.isEmpty()) {
            return ResponseEntity.status(ErrorCode.NOT_FOUND_TOKEN.getHttpStatus())
                    .body(new ApiResponse<>(false, null, ErrorCode.NOT_FOUND_TOKEN.getMessage()));
        }

        try {
            Claims claims = jwtUtil.validateToken(token.get());
            String userId = claims.get("userId", String.class);

            Optional<User> userOptional = UserService.findByUserId(userId);
            if (userOptional.isEmpty()) {
                return ResponseEntity.status(ErrorCode.NOT_FOUND_USER.getHttpStatus())
                        .body(new ApiResponse<>(false, null, ErrorCode.NOT_FOUND_USER.getMessage()));
            }

            User user = userOptional.get();
            Map<String, Object> responseData = Map.of(
                    "userId", user.getUserId(),
                    "userName", user.getUserName(),
                    "userRole", user.getUserRole()
            );

            return ResponseEntity.ok(new ApiResponse<>(true, responseData, null));

        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(ErrorCode.EXPIRED_TOKEN_ERROR.getHttpStatus())
                    .body(new ApiResponse<>(false, null, ErrorCode.EXPIRED_TOKEN_ERROR.getMessage()));
        } catch (JwtException e) {
            return ResponseEntity.status(ErrorCode.NOT_TOKEN_INVALID.getHttpStatus())
                    .body(new ApiResponse<>(false, null, ErrorCode.NOT_TOKEN_INVALID.getMessage()));
        }
    }
    // ✅ 마이페이지에 개인정보 GET 호출하기
    @GetMapping("/info")
    @ResponseBody
    public ResponseEntity<?> getUsersInfo(HttpServletRequest request) {
        Optional<String> token = HeaderUtil.refineHeader(request, "Authorization", "Bearer ");
        if (token.isEmpty()) {
            return ResponseEntity.status(ErrorCode.NOT_FOUND_TOKEN.getHttpStatus())
                    .body(new ApiResponse<>(false, null, ErrorCode.NOT_FOUND_TOKEN.getMessage()));
        }
        try {
            Claims claims = jwtUtil.validateToken(token.get());
            String userId = claims.get("userId", String.class);

            Optional<User> userOptional = UserService.findByUserId(userId);
            if (userOptional.isEmpty()) {
                return ResponseEntity.status(ErrorCode.NOT_FOUND_USER.getHttpStatus())
                        .body(new ApiResponse<>(false, null, ErrorCode.NOT_FOUND_USER.getMessage()));
            }

            User user = userOptional.get();
            Map<String, Object> responseData = Map.of(
                    "userId", user.getUserId(),
                    "userName", user.getUserName(),
                    "userEmail", user.getUserEmail(),
                    "userBirthday", user.getUserBirthday(),
                    "userCreatedAt", user.getUserCreatedAt()
            );

            return ResponseEntity.ok(new ApiResponse<>(true, responseData, null));

        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(ErrorCode.EXPIRED_TOKEN_ERROR.getHttpStatus())
                    .body(new ApiResponse<>(false, null, ErrorCode.EXPIRED_TOKEN_ERROR.getMessage()));
        } catch (JwtException e) {
            return ResponseEntity.status(ErrorCode.NOT_TOKEN_INVALID.getHttpStatus())
                    .body(new ApiResponse<>(false, null, ErrorCode.NOT_TOKEN_INVALID.getMessage()));
        }
    }
    @DeleteMapping("/users")
    public ResponseEntity<?> deleteUser(HttpServletRequest request) {
        try {
            String userId = JwtUtil.extractUserIdFromRequest(request);
            userService.deleteUserById(userId);
            return ResponseEntity.ok().body("탈퇴 성공");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("탈퇴 실패");
        }
    }

}