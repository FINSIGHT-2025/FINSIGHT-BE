package com.finsight.backend.enumerate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    NOT_END_POINT(1000, HttpStatus.BAD_REQUEST, "End Point가 존재하지 않습니다."),
    NOT_FOUND_USER(1100, HttpStatus.BAD_REQUEST, "회원 정보를 찾을 수 없습니다."),

    NOT_TOKEN_INVALID(2000, HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
    TOKEN_MALFORMED_ERROR(2001, HttpStatus.UNAUTHORIZED, "토큰이 올바르지 않습니다."),
    TOKEN_TYPE_ERROR(2002, HttpStatus.UNAUTHORIZED, "토큰 타입이 일치하지 않습니다."),
    EXPIRED_TOKEN_ERROR(2003, HttpStatus.UNAUTHORIZED, "만료된 토큰입니다"),
    TOKEN_UNSUPPORTED_ERROR(2004, HttpStatus.UNAUTHORIZED, "지원하지 않는 토큰입니다."),
    TOKEN_UNKNOWN_ERROR(2005, HttpStatus.UNAUTHORIZED, "알수 없는 토큰입니다.");

//    ACCESS_DENIED_ERROR(4001, HttpStatus.FORBIDDEN, "접근 권한이 없습니다.");  권한 존재가 없기 때문에 노필요

    private final Integer code;
    private final HttpStatus httpStatus;
    private final String message;
}
