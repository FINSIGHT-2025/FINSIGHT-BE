package com.finsight.backend.security.filter;

import com.finsight.backend.enumerate.ErrorCode;
import com.finsight.backend.security.JwtAuthenticationProvider;
import com.finsight.backend.security.JwtAuthenticationToken;
import com.finsight.backend.security.info.JwtUserInfo;
import com.finsight.backend.util.HeaderUtil;
import com.finsight.backend.util.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@AllArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final JwtAuthenticationProvider jwtAuthenticationProvider;

    private final String[] whiteList = {"/users", "/users/email", "/users/login", "/"};
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String uri = request.getRequestURI();
        log.info("JwtFilter 호출됨 : {}", request.getRequestURI());
        for (String white : whiteList) {
            if (uri.equals(white)) {
                filterChain.doFilter(request, response);
                return;
            }
        }

        final String token = HeaderUtil.refineHeader(request, "accessToken", "Bearer ")
                .orElseThrow(() -> new RuntimeException(ErrorCode.NOT_TOKEN_INVALID.getMessage()));

        final Claims claims = jwtUtil.validateToken(token);
        JwtUserInfo jwtUserInfo = new JwtUserInfo(
                String.valueOf(claims.get("userId", String.class)),
                String.valueOf(claims.get("username", String.class)),
                String.valueOf(claims.get("nickname", String.class))
        );

        JwtAuthenticationToken jwtAuthenticationToken = new JwtAuthenticationToken(
                null,
                jwtUserInfo.id(),
                jwtUserInfo.username(),
                jwtUserInfo.nickname()
        );
        UsernamePasswordAuthenticationToken afterAuthentication = (UsernamePasswordAuthenticationToken) jwtAuthenticationProvider.authenticate(jwtAuthenticationToken);
        afterAuthentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        final SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(afterAuthentication);
        SecurityContextHolder.setContext(securityContext);

        filterChain.doFilter(request, response);
    }
}
