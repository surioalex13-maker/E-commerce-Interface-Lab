package com.ws101.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import java.time.LocalDateTime;

/**
 * Global exception handler for the REST API.
 * 
 * Catches and handles all exceptions across the application, providing
 * consistent error response formats with appropriate HTTP status codes.
 * 
 * @author Pair Programming Team
 */
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Handles ProductNotFoundException.
     * 
     * Returns 404 Not Found status when a requested product does not exist.
     * 
     * @param ex the ProductNotFoundException that was thrown
     * @param request the current web request
     * @return ResponseEntity with 404 status and error details
     */
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleProductNotFoundException(
            ProductNotFoundException ex,
            WebRequest request) {

        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                "PRODUCT_NOT_FOUND",
                ex.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles InvalidProductException.
     * 
     * Returns 400 Bad Request status when invalid product data is provided.
     * 
     * @param ex the InvalidProductException that was thrown
     * @param request the current web request
     * @return ResponseEntity with 400 status and error details
     */
    @ExceptionHandler(InvalidProductException.class)
    public ResponseEntity<ErrorResponse> handleInvalidProductException(
            InvalidProductException ex,
            WebRequest request) {

        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "INVALID_PRODUCT_DATA",
                ex.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles all other unexpected exceptions.
     * 
     * Returns 500 Internal Server Error for unhandled exceptions.
     * 
     * @param ex the Exception that was thrown
     * @param request the current web request
     * @return ResponseEntity with 500 status and error details
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(
            Exception ex,
            WebRequest request) {

        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "INTERNAL_SERVER_ERROR",
                "An unexpected error occurred. Please try again later.",
                request.getDescription(false).replace("uri=", "")
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
