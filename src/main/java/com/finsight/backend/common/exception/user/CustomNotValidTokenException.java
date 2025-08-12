package com.finsight.backend.common.exception.user;

import com.finsight.backend.common.exception.BusinessException;
import com.finsight.backend.common.exception.ErrorCode;

public class CustomNotValidTokenException extends BusinessException {
    public CustomNotValidTokenException() {
        super(ErrorCode.NOT_TOKEN_INVALID);
    }
}
