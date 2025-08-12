package com.finsight.backend.domain.vo.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

/*
이메일 인증 코드와 그 만료 시간을 저장하는 데 사용되는 VO(Value Object) 역할
즉 이메일마다 어떤 인증코드가 언제까지 유효한지를 저장하는 용도
 */
@AllArgsConstructor
public class EmailVerificationVO {
    @Getter
    private String code;
    private Long expireAt;

    public boolean isExpired() {
        return System.currentTimeMillis() > expireAt;
    }
}