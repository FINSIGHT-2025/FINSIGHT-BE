package com.finsight.backend.common.exception;

public class CustomNotFoundException extends BusinessException{
    public CustomNotFoundException() {
    }

    public CustomNotFoundException(String message) {
        super(message);
    }
}
