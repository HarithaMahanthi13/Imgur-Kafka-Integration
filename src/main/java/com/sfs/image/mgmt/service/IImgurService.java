package com.sfs.image.mgmt.service;

import org.springframework.web.multipart.MultipartFile;
import com.sfs.image.mgmt.entity.Image;

import java.io.IOException;
import java.util.List;

/**
 * Service interface for managing Imgur operations.
 */
public interface IImgurService {

    /**
     * Uploads an image to Imgur.
     *
     * @param file the image file to be uploaded
     * @param username the username of the user uploading the image
     * @param password the password of the user uploading the image
     * @return the uploaded Image entity
     * @throws IOException if an I/O error occurs during the upload process
     */
    Image uploadImage(MultipartFile file, String username, String password) throws IOException;

    /**
     * Retrieves a list of images associated with a user.
     *
     * @param username the username of the user whose images are to be retrieved
     * @param password the password of the user whose images are to be retrieved
     * @return a list of images associated with the user
     */
    List<Image> getUserImages(String username, String password);

    /**
     * Deletes an image from Imgur.
     *
     * @param id the ID of the image to be deleted
     * @param username the username of the user requesting the deletion
     * @param password the password of the user requesting the deletion
     */
    void deleteImage(Long id, String username, String password);
}
