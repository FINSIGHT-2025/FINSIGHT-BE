package com.finsight.backend.common.exception.product;

import com.finsight.backend.common.exception.BusinessException;
import com.finsight.backend.common.exception.ErrorCode;

public class CustomNotFoundProduct extends BusinessException {
    public CustomNotFoundProduct() {
        super(ErrorCode.NOT_FOUND_PRODUCT);
    }
}
