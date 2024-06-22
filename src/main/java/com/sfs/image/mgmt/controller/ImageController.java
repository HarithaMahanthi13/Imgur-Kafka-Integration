package com.sfs.image.mgmt.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import lombok.extern.slf4j.Slf4j;

import com.sfs.image.mgmt.entity.Image;
import com.sfs.image.mgmt.service.IImgurService;

@RestController
@RequestMapping("/sfs/images")
@Slf4j
public class ImageController {

    @Autowired
    private IImgurService imgurService;

    /**
     * Uploads an image to Imgur and associates it with the user.
     *
     * @param file the image file to upload
     * @param username the username for authentication
     * @param password the password for authentication
     * @return a ResponseEntity containing the uploaded Image
     * @throws IOException if an error occurs during file upload
     */
    @PostMapping("/upload")
    public ResponseEntity<Image> uploadImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam("username") String username,
            @RequestParam("password") String password) throws IOException {
        Image image = imgurService.uploadImage(file, username, password);
        return ResponseEntity.ok(image);
    }

    /**
     * Retrieves all images associated with a user.
     *
     * @param username the username for authentication
     * @param password the password for authentication
     * @return a ResponseEntity containing a list of images
     */
    @GetMapping
    public ResponseEntity<List<Image>> getUserImages(
            @RequestParam("username") String username,
            @RequestParam("password") String password) {
        List<Image> images = imgurService.getUserImages(username, password);
        System.out.println("count of images: " + images.size());
        return ResponseEntity.ok(images);
    }

    /**
     * Deletes an image by its ID.
     *
     * @param id the ID of the image to delete
     * @param username the username for authentication
     * @param password the password for authentication
     * @return a ResponseEntity indicating the result of the delete operation
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteImage(
            @PathVariable Long id,
            @RequestParam("username") String username,
            @RequestParam("password") String password) {
        imgurService.deleteImage(id, username, password);
        return ResponseEntity.noContent().build();
    }
}
