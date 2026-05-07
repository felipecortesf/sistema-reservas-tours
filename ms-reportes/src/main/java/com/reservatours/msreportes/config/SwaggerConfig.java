package com.reservatours.msreportes.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("ms-reportes API")
                        .version("1.0.0")
                        .description("API del microservicio ms-reportes - Sistema de Reservas de Tours Rio de Janeiro"));
    }
}
