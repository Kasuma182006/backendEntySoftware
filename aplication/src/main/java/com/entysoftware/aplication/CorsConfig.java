package com.entysoftware.aplication;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.lang.NonNull; // En Spring Boot 3 suele mapear desde org.springframework.lang o jakarta

@Configuration
public class CorsConfig {

    private static final String TODOS_LOS_ORIGENES = "*";
    private static final String[] METODOS_HTTP_PERMITIDOS = {"GET", "POST", "PUT", "DELETE", "OPTIONS"};

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@NonNull CorsRegistry registry) { // <-- Se añade @NonNull aquí
                // Permite CORS en todos los endpoints
                registry.addMapping("/**")
                        .allowedOriginPatterns(TODOS_LOS_ORIGENES)
                        .allowedMethods(METODOS_HTTP_PERMITIDOS)
                        .allowedHeaders(TODOS_LOS_ORIGENES)
                        .allowCredentials(true);
            }
        };
    }
}
