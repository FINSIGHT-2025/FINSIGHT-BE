package com.finsight.backend.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    NOT_END_POINT(1000, HttpStatus.BAD_REQUEST, "End Point가 존재하지 않습니다."),
    NOT_FOUND_TOKEN(1006, HttpStatus.BAD_REQUEST, "토큰이 존재하지 않습니다"),
    NOT_FOUND_USER(1100, HttpStatus.BAD_REQUEST, "회원 정보가 존재하지 않습니다."),
    NOT_FORM_INVALID(1101, HttpStatus.BAD_REQUEST, "올바른 형식의 아이디 혹은 비밀번호가 아닙니다."),
    NOT_AUTH_EMAIL(1102, HttpStatus.BAD_REQUEST,"이메일 인증이 완료되지 않았습니다."),
    DUPLICATE_USER(1103, HttpStatus.BAD_REQUEST, "이미 존재하는 회원입니다."),
    NOT_PATH_INVALID(1200, HttpStatus.BAD_REQUEST, "잘못된 경로입니다."),


    NOT_TOKEN_INVALID(2000, HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
    TOKEN_MALFORMED_ERROR(2001, HttpStatus.UNAUTHORIZED, "토큰이 올바르지 않습니다."),
    TOKEN_TYPE_ERROR(2002, HttpStatus.UNAUTHORIZED, "토큰 타입이 일치하지 않습니다."),
    EXPIRED_TOKEN_ERROR(2003, HttpStatus.UNAUTHORIZED, "만료된 토큰입니다"),
    TOKEN_UNSUPPORTED_ERROR(2004, HttpStatus.UNAUTHORIZED, "지원하지 않는 토큰입니다."),
    TOKEN_UNKNOWN_ERROR(2005, HttpStatus.UNAUTHORIZED, "알수 없는 토큰입니다."),
    TOKEN_GENERATE_ERROR(2006, HttpStatus.INTERNAL_SERVER_ERROR, "토큰 생성에 실패했습니다."),
    ;

//    ACCESS_DENIED_ERROR(4001, HttpStatus.FORBIDDEN, "접근 권한이 없습니다.");  권한 존재가 없기 때문에 노필요

    private final Integer code;
    private final HttpStatus httpStatus;
    private final String message;
}
