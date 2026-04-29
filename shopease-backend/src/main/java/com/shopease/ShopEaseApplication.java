package com.shopease;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main Spring Boot application class for ShopEase e-commerce backend.
 * Provides REST API endpoints for product management, orders, and category operations.
 */
@SpringBootApplication
public class ShopEaseApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShopEaseApplication.class, args);
    }
}
