package com.finsight.backend.enumerate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // 1000 ~ 1999 공통(Common) 에러
    // 2000 ~ 2999 인증(Authentication) 에러
    // 3000 ~ 3999 사용자(User) 관련 에러
    // 4000 ~ 4999 데이터/DB 관련 에러
    // 5000 ~ 5999 외부 API 관련 에러
    // 위 에러 코드 동일성 지켜주시고 필요하면 추가 삭제해주세요

    NOT_END_POINT(1000, HttpStatus.BAD_REQUEST, "End Point가 존재하지 않습니다."),

    NOT_TOKEN_INVALID(2000, HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),

    NOT_FOUND_USER(3000, HttpStatus.BAD_REQUEST, "회원 정보를 찾을 수 없습니다."),;

    private final Integer code;
    private final HttpStatus httpStatus;
    private final String message;
}
