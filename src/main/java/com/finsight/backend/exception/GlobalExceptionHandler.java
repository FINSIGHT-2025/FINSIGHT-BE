package com.finsight.backend.exception;

import com.finsight.backend.dto.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // @Valid + @RequestBody 유효성 검사 실패
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleBindException(BindException ex){
        Map<String, String> errors = new HashMap<>();
        ex.getFieldErrors()
                .forEach(fieldError -> errors.put(fieldError.getField(), fieldError.getDefaultMessage()));
        log.warn("Validation failed : {}", errors);
        return ResponseEntity.badRequest().body(new ApiResponse<>(Boolean.FALSE, null, "폼 검증 오류 : " + errors));
    }
}
