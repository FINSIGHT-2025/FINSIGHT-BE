package com.finsight.backend.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finsight.backend.dto.ExceptionDto;
import com.finsight.backend.common.exception.ErrorCode;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

public abstract class AbstractAuthenticationFailure {
    private final ObjectMapper objectMapper = new ObjectMapper();
    protected void setErrorResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException{
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(errorCode.getHttpStatus().value());

        HashMap<String, Object> result = new HashMap<>();
        result.put("success", Boolean.FALSE);
        result.put("data", null);
        result.put("error", new ExceptionDto(errorCode, errorCode.getMessage()));

        String json = objectMapper.writeValueAsString(result);
        response.getWriter().write(json);
    }
}
