package com.ws101.exception;

/**
 * Exception thrown when a requested product cannot be found.
 * 
 * This exception is raised when attempting to retrieve, update, or delete
 * a product that does not exist in the system.
 * 
 * @author Pair Programming Team
 */
public class ProductNotFoundException extends RuntimeException {

    /**
     * Constructs a ProductNotFoundException with a detail message.
     * 
     * @param message the detail message explaining why the product was not found
     */
    public ProductNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructs a ProductNotFoundException with a detail message and cause.
     * 
     * @param message the detail message
     * @param cause the cause of the exception
     */
    public ProductNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
