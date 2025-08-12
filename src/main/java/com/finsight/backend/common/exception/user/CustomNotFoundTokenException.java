package com.finsight.backend.common.exception.user;

import com.finsight.backend.common.exception.BusinessException;
import com.finsight.backend.common.exception.ErrorCode;

public class CustomNotFoundTokenException extends BusinessException {
    public CustomNotFoundTokenException() {
        super(ErrorCode.NOT_FOUND_TOKEN);
    }
}
