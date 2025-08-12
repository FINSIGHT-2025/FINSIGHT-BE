package com.finsight.backend.common.exception.user;

import com.finsight.backend.common.exception.BusinessException;
import com.finsight.backend.common.exception.ErrorCode;

public class CustomDuplicateException extends BusinessException {

    public CustomDuplicateException() {
        super(ErrorCode.DUPLICATE_USER);
    }
}
