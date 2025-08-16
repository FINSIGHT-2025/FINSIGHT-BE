package com.finsight.backend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        // JWT 보안 스키마 설정
        SecurityScheme securityScheme = new SecurityScheme()
                .name("Authorization")
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER);

        // JWT 적용
        SecurityRequirement securityRequirement = new SecurityRequirement().addList("Authorization");

        return new OpenAPI()
                .addSecurityItem(securityRequirement)
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("Authorization", securityScheme))
                .info(new Info()
                        .title("FinSight API Documentation")
                        .description("JWT 기반 인증을 지원하는 API 문서")
                        .version("1.0"));
    }
}

//@Configuration
//public class SwaggerConfig {
//
//    private static final String SECURITY_SCHEME_NAME = "BearerAuth";
//
//    @Bean
//    public OpenAPI customOpenAPI() {
//        // JWT 보안 스키마 설정
//        SecurityScheme securityScheme = new SecurityScheme()
//                .name("Authorization")
//                .type(SecurityScheme.Type.HTTP)
//                .scheme("bearer")
//                .bearerFormat("JWT")
//                .in(SecurityScheme.In.HEADER);
//
//        // JWT 적용
//        SecurityRequirement securityRequirement = new SecurityRequirement()
//                .addList(SECURITY_SCHEME_NAME);
//
//        return new OpenAPI()
//                .addSecurityItem(securityRequirement)
//                .components(new io.swagger.v3.oas.models.Components()
//                        .addSecuritySchemes(SECURITY_SCHEME_NAME, securityScheme))
//                .info(new Info()
//                        .title("FinSight API Documentation")
//                        .description("JWT 기반 인증을 지원하는 API 문서")
//                        .version("1.0"));
//    }
//}
