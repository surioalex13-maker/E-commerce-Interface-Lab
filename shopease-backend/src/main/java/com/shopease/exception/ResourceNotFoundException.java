package com.shopease.exception;

/**
 * Custom exception for resource not found scenarios.
 * Typically maps to HTTP 404 response.
 */
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Constructor with message.
     *
     * @param message the error message
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructor with message and cause.
     *
     * @param message the error message
     * @param cause the root cause
     */
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
