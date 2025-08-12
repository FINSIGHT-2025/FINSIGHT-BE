package com.finsight.backend.common.exception.user;

import com.finsight.backend.common.exception.BusinessException;
import com.finsight.backend.common.exception.ErrorCode;

public class CustomGenerateTokenException extends BusinessException {
    public CustomGenerateTokenException() {
        super(ErrorCode.TOKEN_GENERATE_ERROR);
    }
}
