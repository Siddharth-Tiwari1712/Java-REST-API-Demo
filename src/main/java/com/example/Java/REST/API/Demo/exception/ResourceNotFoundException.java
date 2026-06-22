package com.example.Java.REST.API.Demo.exception;

/**
 * Custom Exception Class - Functional Interface Concept
 * 
 * This exception is thrown when a requested resource is not found.
 * It extends RuntimeException making it an unchecked exception.
 */
public class ResourceNotFoundException extends RuntimeException {
    
    public ResourceNotFoundException(String message) {
        super(message);
    }
    
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
