package com.finsight.backend.dto;

import com.finsight.backend.enumerate.ErrorCode;
import lombok.Getter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Getter
public class ExceptionDto {
    @Min(1000)
    private Integer code;
    @NotBlank
    private String message;

    public ExceptionDto(ErrorCode errorCode, String message){
        this.code = errorCode.getCode();
        this.message = message;
    }
}
