package com.daw.adoptauncompanero.config;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// =============================================================
// PDF 6.2 - 3.1. WebConfig
// Hace que las imágenes guardadas en la carpeta /uploads (fuera
// del JAR) sean accesibles vía URL /uploads/<nombre>.jpg
// Estas imágenes son las fotos de los animales (2.3.2.1).
// =============================================================
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${app.upload.dir}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Ruta absoluta de la carpeta uploads
        Path uploadPath = Paths.get(uploadDir).toAbsolutePath();

        registry.addResourceHandler("/uploads/**")
                // file: indica que es ruta del sistema de ficheros (no classpath)
                .addResourceLocations("file:" + uploadPath.toString() + "/");
    }
}
