package com.learning.libs.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web configuration class.
 * Configures CORS settings to allow cross-origin requests during development.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Configure CORS mappings.
     * Allows all origins, methods, and headers for development purposes.
     * NOTE: Restrict these settings in production for security.
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*");
    }
}
