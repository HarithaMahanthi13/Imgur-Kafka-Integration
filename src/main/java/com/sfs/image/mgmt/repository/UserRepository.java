package com.sfs.image.mgmt.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sfs.image.mgmt.entity.User;

/**
 * Repository interface for managing User entities.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a user by their username.
     *
     * @param username the username of the user to be found
     * @return an Optional containing the user if found, or empty if not found
     */
    Optional<User> findBySfsUser(String username);
}
