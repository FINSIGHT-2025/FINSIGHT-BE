package com.finsight.backend.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // @Valid + @RequestBody 예외 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleBindException(MethodArgumentNotValidException ex){
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult()
                .getFieldErrors()
                .forEach(fieldError -> errors.put(fieldError.getField(), fieldError.getDefaultMessage()));
        log.error("[GlobalExceptionHandler] Validation failed : {}", errors);
        return ResponseEntity.badRequest().body(errors);
    }

    // 타입 변환 오류 처리
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String error = String.format("파라미터 '%s'의 타입이 올바르지 않습니다. (value: %s)", ex.getName(), ex.getValue());
        log.error("[GlobalExceptionHandler] Type Mismatch : {}", error);
        Map<String, String> body = Map.of("message", error);
        return ResponseEntity.badRequest().body(body);
    }

    // 비지니스 예외 처리
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<?> handleBusinessException(BusinessException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        Map<String, Object> body = new HashMap<>();
        body.put("message", errorCode.getMessage());
        return ResponseEntity.status(errorCode.getHttpStatus()).body(body);
    }

    // 기타 예외 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception ex) {
        log.error("[GlobalExceptionHandler] Unexpected error", ex);
        Map<String, String> body = Map.of("message", "서버 에러가 발생했습니다.");
        return ResponseEntity.internalServerError().body(body);
    }
}
