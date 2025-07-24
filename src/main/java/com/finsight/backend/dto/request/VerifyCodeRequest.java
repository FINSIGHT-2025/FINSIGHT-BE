package com.finsight.backend.dto.request;

import lombok.Getter;
import lombok.Setter;

/*
이메일 인증 코드 검증 요청을 처리하기 위한 전용 DTO
 */
@Getter
@Setter
public class VerifyCodeRequest {
    private String email;
    private String code;
}

