package com.finsight.backend.security.config;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

/**
 * 웹 애플리케이션이 시작될 때 보안 필터가 적용되도록 보장
 * Spring Security가 Java 기반 설정(Java Config)을 사용할 때 필요
 * web.xml 없이도 springSecurityFilterChain을 서블릿 필터에 자동으로 등록한다
 */
public class SecurityInitializer
        extends AbstractSecurityWebApplicationInitializer {
}
