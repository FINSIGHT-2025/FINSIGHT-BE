package com.finsight.backend.control;

import com.finsight.backend.dto.request.LoginForm;
import com.finsight.backend.dto.response.LoginResponse;
import com.finsight.backend.service.MemberService;
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

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginForm loginForm, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            log.error("bindingResult = {}", bindingResult.getAllErrors());
            return ResponseEntity.badRequest()
                    .body(bindingResult.getAllErrors());
        }
        Optional<Member> optionalMember = memberService.findMember(loginForm);
        Map<String, Object> result = new HashMap<>();

        if (optionalMember.isPresent()) {
            log.info("member = {}", optionalMember.get().getUsername());
            Member member = optionalMember.get();
            LoginResponse response = new LoginResponse(
                    member.getUserId(),
                    member.getUsername(),
                    member.getNickname(),
                    member.getEmail(),
                    member.getRole()
            );
            result.put("success", true);
            result.put("data", response);
            return ResponseEntity.ok(result);
        } else {
            log.warn("로그인 실패 - 회원 정보 없음");
            return ResponseEntity.status(401).body("Invalid user ID or password");
        }
    }
}
