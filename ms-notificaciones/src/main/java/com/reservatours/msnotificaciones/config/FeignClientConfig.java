package com.reservatours.msnotificaciones.config;

import feign.RequestInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Propaga el header Authorization (JWT) del request entrante hacia las
 * llamadas salientes de OpenFeign (ReservaClient), para que no sean
 * rechazadas con 403 por el SecurityConfig del microservicio destino.
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
