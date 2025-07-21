package com.finsight.security.config;


import com.finsight.security.JwtAuthEntryPoint;
import com.finsight.security.JwtAuthenticationProvider;
import com.finsight.security.filter.JwtAuthenticationFilter;
import com.finsight.security.filter.JwtExceptionFilter;
import com.finsight.security.handler.CustomLogOutProcessHandler;
import com.finsight.security.handler.CustomLogOutResultHandler;
import com.finsight.security.handler.JwtAccessDeniedHandler;
import com.finsight.security.service.CustomUserDetailService;
import com.finsight.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig{
    private final CustomLogOutProcessHandler customLogOutProcessHandler;
    private final CustomLogOutResultHandler customLogOutResultHandler;
    private final JwtAuthEntryPoint jwtAuthEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtUtil jwtUtil;
    private final CustomUserDetailService customUserDetailService;
    // Spring Security와 Spring MVC의 URL 매핑 간 연결고리(중간다리)
    // Spring Security가 자체 URL 매칭 방식 대신 Spring MVC의 핸들러 매핑을 공유해서 사용할 수 있도록 도와줌.
    // MvcMatcher() 또는 MvcRequestMatcher 같은 보안 설정 시 필수
    @Bean
    public HandlerMappingIntrospector mvcHandlerMappingIntrospector(){
        return new HandlerMappingIntrospector();
    }
    // 모든 요청과 응답을 UTF-8 인코딩 적용 -> 한글 깨짐 방지
    @Bean
    public CharacterEncodingFilter characterEncodingFilter(){
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding("UTF-8");
        filter.setForceEncoding(true);
        return filter;
    }
    // 비밀번호 암호화
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    // AuthenticationManager 등록 -> AuthenticationProvider에게 인증을 위임해줌
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception{
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/user/login", "/user/signup").permitAll()  // 로그인, 회원가입은 모두 접근 가능
                        .anyRequest().authenticated()  // 인증된 사용자만 나머지 접근 가능
                )
                .logout(auth -> auth
                        .logoutUrl("/users/logout")
                        .addLogoutHandler(customLogOutProcessHandler)      // 로그아웃 동작을 수행할 핸들러
                        .logoutSuccessHandler(customLogOutResultHandler)   // 로그아웃이 정상적으로 완료된 후 동작할 핸들러
                        .deleteCookies("accessToken"))
                .exceptionHandling(configurer ->
                        configurer
                                .authenticationEntryPoint(jwtAuthEntryPoint)  // 인증되지 않은 사용자 접근 처리
                                .accessDeniedHandler(jwtAccessDeniedHandler)  // 인증은 됐지만 권한이 부족한 경우
                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtUtil, new JwtAuthenticationProvider(customUserDetailService)), LogoutFilter.class)
                .addFilterBefore(new JwtExceptionFilter(), JwtAuthenticationFilter.class)
                .getOrBuild();
    }
}
