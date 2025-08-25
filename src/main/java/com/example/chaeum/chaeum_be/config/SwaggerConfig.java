package com.example.chaeum.chaeum_be.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .title("2025-likelion-chaeum API") //이름
                .description("채움, 비어있는 공간에 가치를 더하다."); //설명

        Server httpsServer = new Server().url("https://egaeuni.shop");


        return new OpenAPI()
                .info(info)
                .servers(List.of(httpsServer));

    }

}
