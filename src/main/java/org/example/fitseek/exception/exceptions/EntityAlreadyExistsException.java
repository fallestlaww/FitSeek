package org.example.fitseek.exception.exceptions;

/**
 * Custom exception that is thrown in case of creating entity already existed in the system
 */
public class EntityAlreadyExistsException extends RuntimeException {
    public EntityAlreadyExistsException(String message) {
        super(message);
    }
}
