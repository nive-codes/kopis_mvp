package com.urbmi.mvp.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author nive
 * @class SwaggerConfig
 * @desc [클래스 설명]
 * @since 2025-01-15
 */
@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(apiInfo());
    }

    private Info apiInfo() {
        return new Info()
                .title("SpringBoot 3.4.1 + Gradle + YML + Mybatis API Documentation")
                .description("This is the API documentation for SpringBoot 3.4.1 Rest API System")
                .version("1.0.0");
    }
}