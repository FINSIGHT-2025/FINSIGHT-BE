package com.finsight.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
/*
/*<mvc:annotation-driven/>
@Controller, @RequestMapping등 인식

RequestMappingHandlerMapping, RequestMappingHandlerAdapter 자동 등록
@RequestBody, @ResponseBody등을 위한 HttpMessageConverters 자동 등록 (ex: JSON 변환)
@Valid 지원
기본 Formatter, Validator 등록
 */
@EnableWebMvc
@ComponentScan(
        basePackages = {"com.finsight.backend"}
        , excludeFilters = @ComponentScan.Filter(
            type = FilterType.ANNOTATION,
            classes = Configuration.class
        )
)

public class ServletConfig implements WebMvcConfigurer {
//    JSP안함
//    @Bean
//    public InternalResourceViewResolver viewResolver() {
//        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
//        resolver.setPrefix("/WEB-INF/views/");
//        resolver.setSuffix(".jsp");
//        return resolver;
//    }

//    HTML안함
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        //정적 파일을 외부 URL로 접근 가능하게 연결하는 설정.
//        registry.addResourceHandler("/r/**")
//                .addResourceLocations("/resources/");
//    }

//    스프링시큐리티 SecurityConfig에서 CorsFilter빈으로 대신한다
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")  // 모든 요청 경로에 대해
//                .allowedOrigins("http://localhost:5173")  // 허용할 Origin
//                .allowedMethods("GET", "POST", "PUT", "DELETE")  // 허용할 HTTP 메서드
//                .allowedHeaders("*")
//                .allowCredentials(true)  // 인증 정보 허용 여부
//                .maxAge(3600);  // pre-flight 요청 캐시 시간 (초)
//    }

//  Servlet 3.0 파일 업로드 사용시
    @Bean
    public MultipartResolver multipartResolver() {
        StandardServletMultipartResolver resolver = new StandardServletMultipartResolver();
        return resolver;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry){
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:5173")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}