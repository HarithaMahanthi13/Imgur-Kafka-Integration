package com.sfs.image.mgmt.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import lombok.extern.slf4j.Slf4j;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sfs.image.mgmt.entity.Image;
import com.sfs.image.mgmt.entity.ProducerMessage;
import com.sfs.image.mgmt.entity.ResponseMessage;
import com.sfs.image.mgmt.kakfa.KafkaProducer;
import com.sfs.image.mgmt.service.IImgurService;
import com.sfs.image.mgmt.service.KafkaImageUploadService;

@RestController
@RequestMapping("/sfs/images")
@Slf4j
public class ImageController {

    @Autowired
    private IImgurService imgurService;
    
  
    private final KafkaImageUploadService kafkaImageUploadService;
    
    private final KafkaProducer kafkaProducer;
    private final ObjectMapper objectMapper;

    @Autowired
    public ImageController(KafkaImageUploadService imageUploadService,KafkaProducer kafkaProducer,ObjectMapper objectMapper) {
        this.kafkaImageUploadService = imageUploadService;
        this.kafkaProducer = kafkaProducer;
        this.objectMapper = objectMapper;
    }
    
   

    /**
     * Uploads an image to Imgur and associates it with the user.
     * Post image and image data  to Kafka
     * @param file the image file to upload
     * @param username the username for authentication
     * @param password the password for authentication
     * @return a ResponseEntity containing the uploaded Image
     * @throws IOException if an error occurs during file upload
     */
    @PostMapping("/upload")
    public ResponseEntity<ResponseMessage> uploadImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam("username") String username,
            @RequestParam("password") String password) throws IOException {
    	ResponseMessage responseMessage=new ResponseMessage();
    	StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Image image = imgurService.uploadImage(file, username, password);
        responseMessage.setId(image.getId());
        responseMessage.setImgurId( image.getImgurId());
        responseMessage.setMessage("Uploaded Success");
        responseMessage.setUser(username);
        responseMessage.setImageLink(image.getLink());
 
        
        ProducerMessage producerMessage=new ProducerMessage();
    	producerMessage.setUsername(username);
    	producerMessage.setImageName(username+"Image");
    	producerMessage.setImageBytes(file.getBytes());
    	
      
    	 try {
             String jsonMessage = objectMapper.writeValueAsString(producerMessage);
              kafkaProducer.sendMessage(jsonMessage);
         } catch (JsonProcessingException e) {
             e.printStackTrace();
           
         }
    	 stopWatch.stop();
         log.info("Time taken to upload image: " + stopWatch.getTotalTimeMillis() + " ms");
        return ResponseEntity.ok(responseMessage);
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
    public ResponseEntity<ResponseMessage> deleteImage(
            @PathVariable Long id,
            @RequestParam("username") String username,
            @RequestParam("password") String password) {
        imgurService.deleteImage(id, username, password);
        return ResponseEntity.noContent().build();
    }
}
