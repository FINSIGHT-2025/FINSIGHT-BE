// 2025-07-29 JY
// 2025-08-13 JY Refactoring
package com.finsight.backend.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class InvTestException extends RuntimeException {
    private static final HttpStatus DEFAULT_STATUS = HttpStatus.BAD_REQUEST;
    private HttpStatus httpStatus;

    public InvTestException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = (httpStatus != null) ? httpStatus : DEFAULT_STATUS;
    }
}