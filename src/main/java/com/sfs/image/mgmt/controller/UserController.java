package com.sfs.image.mgmt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sfs.image.mgmt.dto.UserProfile;
import com.sfs.image.mgmt.entity.User;
import com.sfs.image.mgmt.exception.AuthenticationException;
import com.sfs.image.mgmt.exception.UserNotFoundException;
import com.sfs.image.mgmt.service.IUserService;

@RestController
@RequestMapping("/sfs/users")
public class UserController {

    @Autowired
    private IUserService userService;

    /**
     * Registers a new user.
     *
     * This endpoint allows for the registration of a new user by accepting a User object
     * in the request body and passing it to the userService for registration.
     *
     * @param user the User object containing user details
     * @return a ResponseEntity containing the registered User object
     */
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.registerUser(user));
    }

    /**
     * Authenticates a user.
     *
     * This endpoint allows for user authentication by accepting a User object
     * in the request body and passing the credentials to the userService for validation.
     *
     * @param user the User object containing login credentials
     * @return a ResponseEntity containing a success message if authenticated, or an error message if authentication fails
     */
    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody User user) {
        try {
            return ResponseEntity.ok(userService.authenticateUser(user.getSfsUser(), user.getSfsUserpassword()));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("login failure");
        }
        catch (RuntimeException e) {
            // Catching RuntimeException to handle unexpected errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error: " + e.getMessage());
        }
    }

    /**
     * Retrieves a user's profile.
     *
     * This endpoint allows for fetching a user's profile by username. It returns the user's profile
     * including their basic information and associated images.
     *
     * @param username the username of the user
     * @return a ResponseEntity containing the UserProfile object if the user is found, or an error message if not
     */
    @GetMapping("/{username}")
    public ResponseEntity<UserProfile> getUserProfile(@PathVariable String username) {
        try {
            User user = userService.getUser(username).orElseThrow(() -> new UserNotFoundException("User not found"));
            UserProfile userProfileDto = new UserProfile();
            userProfileDto.setId(user.getId());
            userProfileDto.setUsername(user.getSfsUser());
            userProfileDto.setImages(user.getImage());
            return ResponseEntity.ok(userProfileDto);
        } catch (UserNotFoundException e) {
        	System.out.println("incatch block");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
