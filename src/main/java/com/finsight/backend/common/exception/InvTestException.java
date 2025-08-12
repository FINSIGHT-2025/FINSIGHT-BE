// 2025-07-29 JY
package com.finsight.backend.common.exception;

import org.springframework.http.HttpStatus;

public class InvTestException extends RuntimeException {
    private HttpStatus httpStatus;
    public InvTestException(String message) {
        super(message);
        this.httpStatus = HttpStatus.BAD_REQUEST; // 기본값 (예: 400 Bad Request)
    }

    public InvTestException(String message, Throwable cause) {
        super(message, cause);
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR; // 기본값 (예: 500 Internal Server Error)
    }

    // --- 새로 추가된 생성자 ---
    public InvTestException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public InvTestException(String message, Throwable cause, HttpStatus httpStatus) {
        super(message, cause);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}