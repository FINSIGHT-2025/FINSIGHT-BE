package com.finsight.backend.util;

import com.finsight.backend.dto.response.TokenInfoDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
@Slf4j
public class JwtUtil implements InitializingBean {
    private static final Integer ACCESS_TOKEN_MS = 60 * 60 * 1000;
    private static final String USER_ID_CLAIM_NAME = "userId";
    private static final String USER_USERNAME_CLAIM_NAME = "username";

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

        return generateToken(claims, ACCESS_TOKEN_MS);
    }

    public Claims decodeToken(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) {
                throw new IllegalArgumentException("JWT 형식이 올바르지 않습니다.");
            }

            String payload = new String(Decoders.BASE64URL.decode(parts[1]));
            return Jwts.parserBuilder()
                    .build()
                    .parseClaimsJwt(parts[0] + "." + parts[1] + ".")
                    .getBody();
        } catch (Exception e) {
            log.error("JWT 디코딩 실패", e);
            throw new IllegalArgumentException("유효하지 않은 JWT 토큰입니다.");
        }
    }
}