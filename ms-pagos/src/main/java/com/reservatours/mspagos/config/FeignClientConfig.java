package com.reservatours.mspagos.config;

import feign.RequestInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Propaga el header Authorization (JWT) del request entrante hacia las
 * llamadas salientes de OpenFeign (ReservaClient).
 *
 * Sin esto, las llamadas Feign entre microservicios viajan sin token y
 * son rechazadas con 403 por el SecurityConfig del microservicio destino
 * en cualquier endpoint protegido (ej: GET /api/v1/reservas/{id}).
 */
@Configuration
public class FeignClientConfig {

    @Bean
    public RequestInterceptor authorizationHeaderInterceptor() {
        return template -> {
            ServletRequestAttributes attributes =
                    (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                String authHeader = request.getHeader("Authorization");
                if (authHeader != null) {
                    template.header("Authorization", authHeader);
                }
            }
        };
    }
}
