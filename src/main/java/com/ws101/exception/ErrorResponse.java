package com.ws101.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

/**
 * Standardized error response format for API errors.
 * 
 * This class defines the structure of all error responses returned by the API,
 * ensuring consistency and providing clients with clear error information.
 * 
 * @author Pair Programming Team
 */
@AllArgsConstructor
@Getter
@Setter
public class ErrorResponse {

    /**
     * Timestamp when the error occurred.
     * Formatted as ISO-8601 date-time string.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;

    /**
     * HTTP status code associated with the error.
     */
    private int status;

    /**
     * Error code identifier for programmatic handling.
     */
    private String errorCode;

    /**
     * Human-readable error message describing the issue.
     */
    private String message;

    /**
     * Path of the API endpoint where the error occurred.
     */
    private String path;

}
