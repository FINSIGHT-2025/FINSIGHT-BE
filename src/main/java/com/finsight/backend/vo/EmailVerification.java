package com.finsight.backend.vo;

import lombok.Getter;

/*
이메일 인증 코드와 그 만료 시간을 저장하는 데 사용되는 VO(Value Object) 역할
즉 이메일마다 어떤 인증코드가 언제까지 유효한지를 저장하는 용도
 */
public class EmailVerification {
    @Getter
    private String code;
    private long expireAt;

    public EmailVerification(String code, long expireAt) {
        this.code = code;
        this.expireAt = expireAt;
    }

    public boolean isExpired() {
        return System.currentTimeMillis() > expireAt;
    }
}