package com.shopease.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * CORS Configuration for the ShopEase backend.
 * Enables Cross-Origin Resource Sharing to allow frontend requests from different origins.
 * This configuration allows the frontend (typically running on localhost:5500)
 * to communicate with the backend (running on localhost:8080).
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Configure global CORS mappings for all endpoints.
     * Allows the frontend to make requests to the backend API.
     *
     * @param registry the CORS registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins(
                        "http://localhost:5500",
                        "http://localhost:3000",
                        "http://localhost:8080",
                        "http://127.0.0.1:5500",
                        "http://127.0.0.1:3000"
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
                .allowedHeaders("Authorization", "Content-Type", "Accept")
                .allowCredentials(true)
                .maxAge(3600);

        // Optional: Allow all endpoints with less restrictive CORS
        registry.addMapping("/api/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
                .allowedHeaders("*")
                .allowCredentials(false)
                .maxAge(3600);
    }
}
