package org.example.fitseek.exception.exceptions;

/**
 * Custom exception thrown if given entity is null
 */
public class EntityNullException extends RuntimeException {
    public EntityNullException(String message) {
        super(message);
    }
}
