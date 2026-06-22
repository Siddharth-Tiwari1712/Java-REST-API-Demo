package com.example.Java.REST.API.Demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

/**
 * CONTROLLER ADVICE - Global Exception Handler
 * 
 * This class centralizes exception handling for the entire application.
 * Using @ControllerAdvice, we can handle exceptions globally instead of
 * handling them in individual controllers.
 * 
 * Benefits:
 * - Consistent error responses across the application
 * - Centralized error handling logic
 * - Reduces code duplication
 * - Easier maintenance and modification
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    
    /**
     * Handle ResourceNotFoundException
     * Returns 404 NOT_FOUND HTTP status code
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
            ResourceNotFoundException ex,
            WebRequest request) {
        
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            ex.getMessage(),
            LocalDateTime.now(),
            request.getDescription(false).replace("uri=", "")
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
    
    /**
     * Handle IllegalArgumentException
     * Returns 400 BAD_REQUEST HTTP status code
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
            IllegalArgumentException ex,
            WebRequest request) {
        
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            ex.getMessage(),
            LocalDateTime.now(),
            request.getDescription(false).replace("uri=", "")
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    
    /**
     * Global exception handler for any other exceptions
     * Returns 500 INTERNAL_SERVER_ERROR HTTP status code
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(
            Exception ex,
            WebRequest request) {
        
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "An error occurred: " + ex.getMessage(),
            LocalDateTime.now(),
            request.getDescription(false).replace("uri=", "")
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
