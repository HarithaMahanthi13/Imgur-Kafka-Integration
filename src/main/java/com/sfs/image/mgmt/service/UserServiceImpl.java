package com.sfs.image.mgmt.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sfs.image.mgmt.entity.User;
import com.sfs.image.mgmt.exception.AuthenticationException;
import com.sfs.image.mgmt.exception.UserNotFoundException;
import com.sfs.image.mgmt.repository.UserRepository;

/**
 * Service class for managing users.
 */
@Service("userService")
public class UserServiceImpl implements IUserService {
	
	@Autowired
	private UserRepository userRepository;
	
    /**
     * Registers a new user in the system.
     *
     * @param user The user details to register. Assumes password is set and is in clear text.
     * @return The registered user with persisted state (e.g., assigned ID).
    */
	public User registerUser(User user) {
		
		return userRepository.save(user);
	}
	
    /**
     * Authenticates a user based on username and password.
     *
     * @param username The username of the user trying to log in.
     * @param password The clear text password provided for login.
     * @return The authenticated user if credentials are valid.
     * @throws RuntimeException if no matching user is found or if the password does not match.
     */
		public String authenticateUser(String username,String password) {
			
			 Optional<User> optionalUser = userRepository.findBySfsUser(username);
		        User user = optionalUser.orElseThrow(() -> new UserNotFoundException("User not found"));
	
		        if (user.getSfsUserpassword().equals(password)) {
		            return "login success";
		        }
		        else{
		        	 throw new AuthenticationException("Invalid username or password");}
		        }
	
		
		 public Optional<User> getUser(String username) {
			 Optional<User> optionalUser = userRepository.findBySfsUser(username);
		        User user = optionalUser.orElseThrow(() -> new UserNotFoundException("User not found"));
	
		        return userRepository.findBySfsUser(username);
		    }


}
