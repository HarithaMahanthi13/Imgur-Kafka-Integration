package com.sfs.image.mgmt.exception;

/**
 * Custom exception class for handling user-not-found errors.
 */
public class UserNotFoundException extends RuntimeException {

    /**
     * Initializes a new instance of the UserNotFoundException class with a specified error message.
     *
     * @param message The error message that describes the reason for the exception.
     */
    public UserNotFoundException(String message) {
        super(message);
    }
}
