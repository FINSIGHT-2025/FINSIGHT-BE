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
    @Bean
    public MultipartResolver multipartResolver() {
        StandardServletMultipartResolver resolver = new StandardServletMultipartResolver();
        return resolver;
    }

}