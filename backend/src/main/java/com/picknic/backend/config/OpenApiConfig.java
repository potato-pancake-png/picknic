package com.picknic.backend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * OpenAPI (Swagger) 설정
 * API 문서 자동 생성을 위한 설정 클래스
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI picknicOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Picknic API")
                        .description("Picknic 청소년 투표 커뮤니티 - Point System & Leaderboard API")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("Picknic Team")
                                .email("support@picknic.com")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Local Development Server"),
                        new Server()
                                .url("https://api.picknic.com")
                                .description("Production Server")
                ));
    }
}
