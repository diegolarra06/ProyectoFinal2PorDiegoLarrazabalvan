package com.daw.adoptauncompanero.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// =============================================================
// CONFIGURACIÓN CORS
// Permite que el frontend Vue (puerto 5173) pueda hacer
// peticiones al backend Spring Boot (puerto 8080) sin que el
// navegador las bloquee por la política Same-Origin.
// =============================================================

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
        .allowedOrigins("http://localhost:5173")
        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
        .allowedHeaders("*")
        .allowCredentials(true);
    }
}
