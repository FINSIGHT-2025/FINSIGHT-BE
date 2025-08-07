package com.finsight.backend.security.config;

import com.finsight.backend.security.JwtAuthenticationProvider;
import com.finsight.backend.security.JwtEntryPoint;
import com.finsight.backend.security.filter.JwtAuthenticationFilter;
import com.finsight.backend.security.filter.JwtExceptionFilter;
import com.finsight.backend.security.handler.CustomLogoutResultHandler;
import com.finsight.backend.security.service.CustomerDetailService;
import com.finsight.backend.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
@EnableWebSecurity
@Slf4j
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomLogoutResultHandler customLogoutResultHandler;
    private final JwtEntryPoint jwtEntryPoint;
    private final JwtUtil jwtUtil;
    private final CustomerDetailService customerDetailService;

    @Bean
    public HandlerMappingIntrospector mvcHandlerMappingIntrospector() {
        return new HandlerMappingIntrospector();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
//        return NoOpPasswordEncoder.getInstance(); //비밀번호 암호와 안하기
    }

    @Bean
    protected SecurityFilterChain securityFilterChain(final HttpSecurity httpSecurity) throws Exception{
        return httpSecurity
                .cors() // cors 설정
                .and()
                .csrf(AbstractHttpConfigurer::disable)  // csrf 비활성화
                .httpBasic(AbstractHttpConfigurer::disable) // 기본 인증 비활성화
                .sessionManagement((sessionManagement) ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))  // 세션 비활성화
                .authorizeHttpRequests(requestMatcherRegistry ->
                        requestMatcherRegistry
                                .requestMatchers("/users", "/users/email", "/users/login", "/users/authcode", "/css/**", "/ws-etf/**", "/ws-etf").permitAll()
                                .anyRequest().authenticated())
                .logout(configurer ->
                        configurer
                                .logoutUrl("/users/logout")
                                .logoutSuccessHandler(customLogoutResultHandler)
                                .deleteCookies()
                )
                .exceptionHandling(configurer ->
                        configurer
                                .authenticationEntryPoint(jwtEntryPoint)
                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtUtil, new JwtAuthenticationProvider(customerDetailService)), LogoutFilter.class)
                .addFilterBefore(new JwtExceptionFilter(), JwtAuthenticationFilter.class)
                .getOrBuild();
    }
}