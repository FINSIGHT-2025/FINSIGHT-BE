package com.finsight.backend.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {
    private String userId;
    private String username;
    private String nickname;
    public JwtAuthenticationToken(Collection<? extends GrantedAuthority> authorities, String userId, String username, String nickname) {
        super(authorities);
        this.userId = userId;
        this.username = username;
        this.nickname = nickname;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return this.userId;
    }
}
