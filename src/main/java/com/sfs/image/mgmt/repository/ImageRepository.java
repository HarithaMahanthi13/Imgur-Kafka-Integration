package com.sfs.image.mgmt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sfs.image.mgmt.entity.Image;
import com.sfs.image.mgmt.entity.User;

/**
 * Repository interface for managing Image entities.
 */
public interface ImageRepository extends JpaRepository<Image, Long> {

    /**
     * Finds a list of images associated with the given user.
     *
     * @param user the user whose images are to be found
     * @return a list of images associated with the user
     */
    List<Image> findByUser(User user);
}
