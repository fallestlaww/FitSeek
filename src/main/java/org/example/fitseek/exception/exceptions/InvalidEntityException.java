package org.example.fitseek.exception.exceptions;

/**
 * Custom exception thrown if given entity is invalid.
 */
public class InvalidEntityException extends RuntimeException {
    public InvalidEntityException(String message) {
        super(message);
    }
}
