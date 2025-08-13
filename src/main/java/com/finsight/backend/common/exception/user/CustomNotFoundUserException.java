package com.finsight.backend.common.exception.user;

import com.finsight.backend.common.exception.BusinessException;
import com.finsight.backend.common.exception.ErrorCode;

public class CustomNotFoundUserException extends BusinessException {

    public CustomNotFoundUserException() {
        super(ErrorCode.NOT_FOUND_USER);
    }
}
