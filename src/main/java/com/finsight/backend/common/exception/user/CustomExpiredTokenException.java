package com.finsight.backend.common.exception.user;

import com.finsight.backend.common.exception.BusinessException;
import com.finsight.backend.common.exception.ErrorCode;

public class CustomExpiredTokenException extends BusinessException {
    public CustomExpiredTokenException() {
        super(ErrorCode.EXPIRED_TOKEN_ERROR);
    }
}
