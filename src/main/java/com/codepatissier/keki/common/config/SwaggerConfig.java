package com.codepatissier.keki.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        Info info = new Info().title("KEKI API")
                .description("KEKI API 명세서입니다.")
                .version("v1.0.0");

        // Security 스키마 설정
        SecurityScheme bearerAuth = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("Bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
                .name("Bearer");

        // Security 요청 설정
        SecurityRequirement addSecurityItem = new SecurityRequirement();
        addSecurityItem.addList("JWT");

        return new OpenAPI()
                .components(new Components().addSecuritySchemes("Bearer", bearerAuth))
                .addSecurityItem(addSecurityItem)
                .info(info);
    }

    @Bean
    public GroupedOpenApi allApi() {
        return GroupedOpenApi.builder()
                .group("all")
                .pathsToMatch("/**")
                .build();
    }

    @Bean
    public GroupedOpenApi usersApi() {
        return GroupedOpenApi.builder()
                .group("users")
                .pathsToMatch("/users/**")
                .build();
    }

    @Bean
    public GroupedOpenApi storesApi() {
        return GroupedOpenApi.builder()
                .group("stores")
                .pathsToMatch("/stores/**")
                .build();
    }

    @Bean
    public GroupedOpenApi postsApi() {
        return GroupedOpenApi.builder()
                .group("posts")
                .pathsToMatch("/posts/**")
                .build();
    }

    @Bean
    public GroupedOpenApi dessertsApi() {
        return GroupedOpenApi.builder()
                .group("desserts")
                .pathsToMatch("/desserts/**")
                .build();
    }

    @Bean
    public GroupedOpenApi calendarsApi() {
        return GroupedOpenApi.builder()
                .group("calendars")
                .pathsToMatch("/calendars/**")
                .build();
    }

    @Bean
    public GroupedOpenApi historiesApi() {
        return GroupedOpenApi.builder()
                .group("histories")
                .pathsToMatch("/histories/**")
                .build();
    }

    @Bean
    public GroupedOpenApi hidesApi() {
        return GroupedOpenApi.builder()
                .group("hides")
                .pathsToMatch("/hides/**")
                .build();
    }

    @Bean
    public GroupedOpenApi noticesApi() {
        return GroupedOpenApi.builder()
                .group("notices")
                .pathsToMatch("/notices/**")
                .build();
    }

    @Bean
    public GroupedOpenApi reportsApi() {
        return GroupedOpenApi.builder()
                .group("report")
                .pathsToMatch("/report/**")
                .build();
    }
}
