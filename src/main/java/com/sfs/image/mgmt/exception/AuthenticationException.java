package com.sfs.image.mgmt.exception;

/**
 * Custom exception class for handling authentication-related errors.
 */
public class AuthenticationException extends RuntimeException {
    
    /**
     * Initializes a new instance of the AuthenticationException class with a specified error message.
     *
     * @param message The error message that describes the reason for the exception.
     */
    public AuthenticationException(String message) {
        super(message);
    }
}

