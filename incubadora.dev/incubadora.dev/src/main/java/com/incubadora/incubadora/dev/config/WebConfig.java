package com.incubadora.incubadora.dev.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**") // Aplica la configuración a todas las rutas bajo /api
                        .allowedOrigins("http://localhost:5173") // Permite peticiones desde el origen de tu frontend
                        .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS") // Permite estos métodos HTTP
                        .allowedHeaders("*") // Permite todas las cabeceras (como Content-Type, Authorization)
                        .allowCredentials(true); // Permite el envío de cookies o cabeceras de autorización
            }
        };
    }
}