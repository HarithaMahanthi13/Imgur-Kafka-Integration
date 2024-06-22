package com.sfs.image.mgmt.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.sfs.image.mgmt.service.KafkaImageUploadService;

@RestController
@RequestMapping("/kafka/images")
public class KafkaProducerController {

    private final KafkaImageUploadService imageUploadService;

    @Autowired
    public KafkaProducerController(KafkaImageUploadService imageUploadService) {
        this.imageUploadService = imageUploadService;
    }

    /**
     * Uploads an image and publishes an event to Kafka.
     *
     * This method accepts a username, image name, and image file, then calls the
     * imageUploadService to handle the image upload and event publishing process.
     *
     * @param username the username associated with the image
     * @param imageName the name of the image
     * @param file the image file to upload
     * @return a status message indicating success or failure
     */
    @PostMapping("/upload")
    public String uploadImage(@RequestParam("username") String username,
                              @RequestParam("imageName") String imageName,
                              @RequestParam("file") MultipartFile file) {
        try {
            imageUploadService.uploadImage(username, imageName, file);
            return "Image uploaded and event published";
        } catch (IOException e) {
            e.printStackTrace();
            return "Image upload failed";
        }
    }
}
