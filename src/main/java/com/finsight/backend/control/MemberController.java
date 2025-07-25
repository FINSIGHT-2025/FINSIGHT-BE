package com.finsight.backend.control;

import com.finsight.backend.dto.request.LoginForm;
import com.finsight.backend.dto.response.LoginResponseWithToken;
import com.finsight.backend.dto.response.TokenInfoDto;
import com.finsight.backend.enumerate.ErrorCode;
import com.finsight.backend.service.MemberService;
import com.finsight.backend.util.JwtUtil;
import com.finsight.backend.vo.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
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
// user123 / securepassword123!
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginForm loginForm){
        Map<String, Object> result = new HashMap<>();
        Optional<Member> optionalMember = memberService.findMember(loginForm);

        if (optionalMember.isPresent()) {
            log.info("member = {}", optionalMember.get().getUsername());
            Member member = optionalMember.get();
            TokenInfoDto tokenInfo = new TokenInfoDto(
                    member.getUserId(),
                    member.getUsername(),
                    member.getNickname()
            );
            String accessToken = jwtUtil.generateAccessToken(tokenInfo);
            result.put("success", Boolean.TRUE);
            result.put("data", new LoginResponseWithToken(
                    member.getEmail(),
                    member.getRole(),
                    accessToken
            ));
            result.put("error", null);
            return ResponseEntity.ok(result);
        } else {
            log.warn("로그인 실패 - 회원 정보 없음");
            result.put("success", Boolean.FALSE);
            result.put("data", "아이디 혹은 비밀번호가 일치하지 않습니다.");
            result.put("error", ErrorCode.NOT_FOUND_USER.getMessage());
            return ResponseEntity.status(ErrorCode.NOT_FOUND_USER.getHttpStatus()).body(result);
        }
    }
}
