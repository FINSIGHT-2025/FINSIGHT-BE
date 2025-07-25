package com.finsight.backend.util;

import com.finsight.backend.dto.response.TokenInfoDto;
import com.finsight.backend.enumerate.Role;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class JwtUtil implements InitializingBean {
    private static final Integer ACCESS_TOKEN_MS = 60 * 60 * 1000;
    private static final String USER_ID_CLAIM_NAME = "userId";
    private static final String USER_USERNAME_CLAIM_NAME = "username";
    private static final String USER_NICKNAME_CLAIM_NAME = "nickname";

    @Value("${jwt.secret}")
    private String secretKey;
    private Key key;
    @Override
    public void afterPropertiesSet() throws Exception {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public Claims validateToken(String token) throws JwtException {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String generateToken(Claims claims,  final Integer expirationPeriod){
        try {
            return Jwts.builder()
                    .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                    .setClaims(claims)
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + expirationPeriod))
                    .signWith(key, SignatureAlgorithm.HS512)
                    .compact();
        } catch (Exception e) {
            log.error("토큰 생성 중 오류", e);
            throw e; // 필요 시 rethrow
        }
    }

    public String generateAccessToken(final TokenInfoDto tokenInfoDto){
        final Claims claims = Jwts.claims();
        claims.put(USER_ID_CLAIM_NAME, tokenInfoDto.getUserId());
        claims.put(USER_USERNAME_CLAIM_NAME, tokenInfoDto.getUsername());
        claims.put(USER_NICKNAME_CLAIM_NAME, tokenInfoDto.getNickname());

        return generateToken(claims, ACCESS_TOKEN_MS);
    }
}