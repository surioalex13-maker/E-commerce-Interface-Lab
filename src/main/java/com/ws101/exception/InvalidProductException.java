package com.ws101.exception;

/**
 * Exception thrown when invalid product data is provided.
 * 
 * This exception is raised when attempting to create or update a product
 * with invalid or incomplete data that fails validation rules.
 * 
 * @author Pair Programming Team
 */
public class InvalidProductException extends RuntimeException {

    /**
     * Constructs an InvalidProductException with a detail message.
     * 
     * @param message the detail message describing the validation error
     */
    public InvalidProductException(String message) {
        super(message);
    }

    /**
     * Constructs an InvalidProductException with a detail message and cause.
     * 
     * @param message the detail message
     * @param cause the cause of the exception
     */
    public InvalidProductException(String message, Throwable cause) {
        super(message, cause);
    }

}
