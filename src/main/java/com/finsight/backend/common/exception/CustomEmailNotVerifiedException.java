package com.finsight.backend.common.exception;

public class CustomEmailNotVerifiedException extends BusinessException{
    public CustomEmailNotVerifiedException() {
    }

    public CustomEmailNotVerifiedException(String message) {
        super(message);
    }
}
