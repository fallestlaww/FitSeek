package org.example.fitseek.exception.exceptions;

/**
 * Custom exception thrown if given entity request is invalid
 */
public class InvalidRequestException extends RuntimeException {
    public InvalidRequestException(String message) {
        super(message);
    }
}
