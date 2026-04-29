package com.shopease.exception;

import lombok.*;

/**
 * Standard error response structure for API exceptions.
 * Used to return consistent error messages to clients.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponse {

    /**
     * HTTP status code.
     */
    private int status;

    /**
     * Error message for the client.
     */
    private String message;

    /**
     * Detailed error information (can include validation errors, etc.).
     */
    private String details;

    /**
     * Timestamp when the error occurred.
     */
    private long timestamp;
}
