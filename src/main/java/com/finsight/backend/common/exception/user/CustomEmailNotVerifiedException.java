package com.finsight.backend.common.exception.user;

import com.finsight.backend.common.exception.BusinessException;
import com.finsight.backend.common.exception.ErrorCode;

public class CustomEmailNotVerifiedException extends BusinessException {

    public CustomEmailNotVerifiedException() {
        super(ErrorCode.NOT_AUTH_EMAIL);
    }
}
