
package com.ingsoftware.validador_fotos.config;

import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final String fotosStoragePath;

    public WebConfig(@Value("${app.storage.fotos.path}") String fotosStoragePath) {
        this.fotosStoragePath = fotosStoragePath;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path storagePath = Paths.get(fotosStoragePath).toAbsolutePath().normalize();

        registry.addResourceHandler("/uploads/fotos/**")
                .addResourceLocations(storagePath.toUri().toString());
    }
}
