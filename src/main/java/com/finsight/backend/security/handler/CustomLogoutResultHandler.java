package com.finsight.backend.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class CustomLogoutResultHandler implements LogoutSuccessHandler {
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setContentType("application/json;charset=UTF-8");
        setSuccessResponse(response);
    }
    private void setSuccessResponse(HttpServletResponse response) throws IOException{
        Map<String, Object> result = new HashMap<>();
        result.put("success", Boolean.TRUE);
        result.put("data", "로그아웃이 완료되었습니다.");
        result.put("error", null);
        String json = objectMapper.writeValueAsString(result);
        response.getWriter().write(json);
    }
}
