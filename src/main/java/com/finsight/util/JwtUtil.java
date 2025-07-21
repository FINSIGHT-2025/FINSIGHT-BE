package com.finsight.util;

import com.finsight.enumerate.Role;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Header;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil implements InitializingBean {
    private static final Integer ACCESS_EXPIRED_MS =  60 * 60 * 1000;  // 액세스 토큰 1시간
    private static final String USER_ID_CLAIM_NAME = "userId";
    private static final String USER_ROLE_CLAIM_NAME = "role";

    @Value("${jwt.secret}")
    private String secretKey;
    private Key key;

    @Override
    public void afterPropertiesSet() throws Exception {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateAccessToken(final Long id, final Role role) {
        final Claims claims = Jwts.claims();
        claims.put(USER_ID_CLAIM_NAME, id.toString());
        claims.put(USER_ROLE_CLAIM_NAME, role.toString());

        return generateToken(claims, ACCESS_EXPIRED_MS);
    }
    public Claims validateToken(final String token) throws JwtException {
        final JwtParser jwtParser = Jwts.parserBuilder()
                .setSigningKey(key)
                .build();

        return jwtParser.parseClaimsJws(token).getBody();
    }
    private String generateToken(Claims claims, final Integer expirationPeriod) {
        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationPeriod))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }
}
