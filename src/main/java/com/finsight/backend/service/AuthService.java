package com.finsight.backend.service;

import com.finsight.backend.common.exception.user.CustomExpiredTokenException;
import com.finsight.backend.common.exception.user.CustomGenerateTokenException;
import com.finsight.backend.common.exception.user.CustomNotFoundTokenException;
import com.finsight.backend.common.exception.user.CustomNotValidTokenException;
import com.finsight.backend.common.util.HeaderUtil;
import com.finsight.backend.common.util.JwtUtil;
import com.finsight.backend.dto.response.TokenInfoDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtUtil jwtUtil;

    public String reGenerateAccessToken(HttpServletRequest request) {
        Optional<String> token = HeaderUtil.refineHeader(request, "Authorization", "Bearer ");

        if(token.isEmpty()){
            log.warn("[AuthService] Not Found Token");
            throw new CustomNotFoundTokenException();
        }

        try{
            Claims claims = jwtUtil.validateToken(token.get());
            TokenInfoDto dto = new TokenInfoDto(
                    claims.get("userId", String.class),
                    claims.get("username", String.class)
            );
            return jwtUtil.generateAccessToken(dto);
        } catch (ExpiredJwtException e) {
            log.warn("[AuthService] Expired Token Error");
            throw new CustomExpiredTokenException();
        } catch (JwtException e) {
            log.warn("[AuthService] Not Token Invalid");
            throw new CustomNotValidTokenException();
        }
    }

    public String isValidToken(HttpServletRequest request) {
        Optional<String> token = HeaderUtil.refineHeader(request, "Authorization", "Bearer ");

        if(token.isEmpty()){
            log.warn("[AuthService] Not Found Token");
            throw new CustomNotFoundTokenException();
        }

        try{
            Claims claims = jwtUtil.validateToken(token.get());
            return claims.get("userId", String.class);
        } catch (ExpiredJwtException e) {
            log.warn("[AuthService] Expired Token Error");
            throw new CustomExpiredTokenException();
        } catch (JwtException e) {
            log.warn("[AuthService] Not Token Invalid");
            throw new CustomNotValidTokenException();
        }
    }

    public String generateAccessToken(TokenInfoDto tokenInfoDto){
        try{
            return jwtUtil.generateAccessToken(tokenInfoDto);
        } catch (JwtException e){
            log.warn("[AuthService] Generate Token Error");
            throw new CustomGenerateTokenException();
        }
    }
}
