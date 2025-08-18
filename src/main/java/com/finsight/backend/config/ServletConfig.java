package com.finsight.backend.config;

import org.springframework.context.annotation.*;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.WebJarsResourceResolver;
import org.springframework.web.servlet.resource.PathResourceResolver;

// ⬇️ Springdoc MVC용 설정들을 명시적으로 Import
@Import({
        org.springdoc.core.SpringDocConfiguration.class,
        org.springdoc.webmvc.core.SpringDocWebMvcConfiguration.class,
        org.springdoc.webmvc.ui.SwaggerConfig.class,
        org.springdoc.core.SwaggerUiConfigProperties.class,
        org.springdoc.core.SwaggerUiOAuthProperties.class,
        org.springdoc.core.SpringDocConfigProperties.class
})
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
        basePackages = {"com.finsight.backend.controller"}
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

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // ✅ Swagger UI 정적 리소스
        registry.addResourceHandler("/swagger-ui/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/swagger-ui/")
                .resourceChain(true)
                .addResolver(new WebJarsResourceResolver())
                .addResolver(new PathResourceResolver());

        // ✅ WebJars 기본 경로
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/")
                .resourceChain(true)
                .addResolver(new WebJarsResourceResolver())
                .addResolver(new PathResourceResolver());

        // (선택) 일부 환경에서 /swagger-ui.html 진입 지원
        registry.addResourceHandler("/swagger-ui.html**")
                .addResourceLocations("classpath:/META-INF/resources/");

    }
}
