package com.finsight.backend.common.exception.others;

import com.finsight.backend.common.exception.BusinessException;
import com.finsight.backend.common.exception.ErrorCode;

public class CustomNotValidPathException extends BusinessException {
    public CustomNotValidPathException() {
        super(ErrorCode.NOT_PATH_INVALID);
    }
}
