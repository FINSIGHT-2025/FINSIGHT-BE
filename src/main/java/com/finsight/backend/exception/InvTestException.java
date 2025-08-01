// 2025-07-29 JY
package com.finsight.backend.exception;

public class InvTestException extends RuntimeException {
    public InvTestException(String message) {
        super(message);
    }
    public InvTestException(String message, Throwable cause) {
        super(message, cause);
    }
}