package com.ws101;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the E-commerce API Spring Boot application.
 * 
 * This application provides a RESTful API for managing products in an 
 * e-commerce system with in-memory data storage.
 * 
 * @author Pair Programming Team
 */
@SpringBootApplication
public class EcommerceApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(EcommerceApiApplication.class, args);
    }

}
