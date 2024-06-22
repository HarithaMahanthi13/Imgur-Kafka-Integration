package com.sfs.image.mgmt.service;

import com.sfs.image.mgmt.entity.User;

import java.util.Optional;

public interface IUserService {

    /**
     * Registers a new user in the system.
     *
     * @param user The user details to register. Assumes password is set and is in clear text.
     * @return The registered user with persisted state (e.g., assigned ID).
     */
    User registerUser(User user);

    /**
     * Authenticates a user based on username and password.
     *
     * @param username The username of the user trying to log in.
     * @param password The clear text password provided for login.
     * @return The authenticated user if credentials are valid.
     * @throws RuntimeException if no matching user is found or if the password does not match.
     */
    String authenticateUser(String username, String password);

    /**
     * Retrieves a user based on the username.
     *
     * @param username The username of the user to retrieve.
     * @return An Optional containing the user if found, or empty if not found.
     */
    Optional<User> getUser(String username);
}

