package com.sfs.image.mgmt.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import com.sfs.image.mgmt.entity.User;
import com.sfs.image.mgmt.exception.AuthenticationException;
import com.sfs.image.mgmt.exception.UserNotFoundException;
import com.sfs.image.mgmt.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * Service class for managing users.
 */
@Service("userService")
@Slf4j
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Registers a new user in the system.
     *
     * This method saves the user details to the repository and returns the registered user.
     *
     * @param user The user details to register. Assumes password is set and is in clear text.
     * @return The registered user with persisted state (e.g., assigned ID).
     */
    public User registerUser(User user) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        User registeredUser = userRepository.save(user);

        stopWatch.stop();
        log.info("Time taken to register user: " + stopWatch.getTotalTimeMillis() + " ms");

        return registeredUser;
    }

    /**
     * Authenticates a user based on username and password.
     *
     * This method checks the provided username and password against the stored user credentials.
     * If the credentials are valid, it returns a success message; otherwise, it throws an exception.
     *
     * @param username The username of the user trying to log in.
     * @param password The clear text password provided for login.
     * @return A success message if the credentials are valid.
     * @throws RuntimeException if no matching user is found or if the password does not match.
     */
    public String authenticateUser(String username, String password) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        try {
            Optional<User> optionalUser = userRepository.findBySfsUser(username);
            User user = optionalUser.orElseThrow(() -> new UserNotFoundException("User not found"));

            if (user.getSfsUserpassword().equals(password)) {
                return "login success";
            } else {
                throw new AuthenticationException("Invalid username or password");
            }
        } finally {
            stopWatch.stop();
            log.info("Time taken to authenticate user: " + stopWatch.getTotalTimeMillis() + " ms");
        }
    }

    /**
     * Retrieves a user by username.
     *
     * This method fetches the user details from the repository based on the provided username.
     * If no matching user is found, it throws an exception.
     *
     * @param username The username of the user to retrieve.
     * @return An Optional containing the user details if found.
     * @throws UserNotFoundException if no matching user is found.
     */
    public Optional<User> getUser(String username) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        try {
            Optional<User> optionalUser = userRepository.findBySfsUser(username);
            optionalUser.orElseThrow(() -> new UserNotFoundException("User not found"));

            return userRepository.findBySfsUser(username);
        } finally {
            stopWatch.stop();
            log.info("Time taken to retrieve user: " + stopWatch.getTotalTimeMillis() + " ms");
        }
    }
}
